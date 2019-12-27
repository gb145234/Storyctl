package fscut.manager.demo.controller;

import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.service.serviceimpl.UserService;
import fscut.manager.demo.util.CsvUtils;
import fscut.manager.demo.util.websocket.WebSocketServer;
import fscut.manager.demo.vo.StoryVO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("story")
public class StoryController {

    @Resource
    private StoryService storyService;

    @Resource
    private UserService userService;

    @Resource
    private CustomerService customerService;

    @Resource
    private MessageService messageService;

    @Resource
    private WebSocketServer webSocketServer;

    @PostMapping("newStory")
    public ResponseEntity newStory(@RequestBody StoryVO storyVO){
        userService.userAllowed(storyVO.getStoryUPK().getProductId());

        Story story = storyService.convertStoryVO2Story(storyVO);

        Optional<Story> optional = storyService.addStory(story);

        Story newStory = null;
        if (optional.isPresent()) {
            newStory = optional.get();
        }
        if (newStory == null) {
            return ResponseEntity.ok("为空！");
        }

        Message message = messageService.addCreateMessage(newStory);

        Integer designId = newStory.getDesignId();
        Integer devId = newStory.getDevId();
        Integer testId = newStory.getTestId();

        if (designId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(designId));
        }
        if (devId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(devId));
        }
        if (testId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(testId));
        }

        return ResponseEntity.ok(newStory);
    }

    @PostMapping("editStory")
    public ResponseEntity editStory(@RequestBody StoryVO storyVO) {
        userService.userAllowed(storyVO.getStoryUPK().getProductId());

        Story story = storyService.convertStoryVO2Story(storyVO);
        Optional<Story> optional = storyService.editStory(story);
        Story updatedStory = null;
        if (optional.isPresent()) {
            updatedStory = optional.get();
        }
        if (updatedStory == null) {
            return ResponseEntity.ok("为空！");
        }

        Message message = messageService.addUpdateMessage(updatedStory);

        Integer designId = updatedStory.getDesignId();
        Integer devId = updatedStory.getDevId();
        Integer testId = updatedStory.getTestId();
        if (designId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(designId));
        }
        if (devId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(devId));
        }
        if (testId != null) {
            WebSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(testId));
        }

        return ResponseEntity.ok(updatedStory);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Page<Story>> showProductStories(@PathVariable("id") Integer id, Integer page, Integer size) {
        userService.userAllowed(id);

        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "putTime");
        Page<Story> storyPage = storyService.getStoriesByProductId(id, user.getUserId(), pageRequest);

        return ResponseEntity.ok(storyPage);
    }

    @GetMapping("Story")
    public ResponseEntity<StoryDetailDTO> showStoryInfo(Integer productId, Integer storyId, Integer edition){
        userService.userAllowed(productId);

        StoryUPK storyUPK = new StoryUPK(productId, storyId, edition);
        StoryDetailDTO storyDetailDTO = storyService.getStoryInfo(storyUPK);
        return ResponseEntity.ok(storyDetailDTO);
    }

    @PostMapping("history")
    public ResponseEntity<List<Story>> showStoryHistory(@RequestBody StoryUPK storyUPK){
        userService.userAllowed(storyUPK.getProductId());

        List<Story> stories = storyService.getStoryHistory(storyUPK);
        return ResponseEntity.ok(stories);
    }

    @DeleteMapping("deleteStory")
    public ResponseEntity<Integer> deleteStory(@RequestBody StoryUPK storyUPK){
        userService.userAllowed(storyUPK.getProductId());

        Integer res = storyService.deleteStory(storyUPK);
        return ResponseEntity.ok(res);
    }

    @PostMapping("selectStory")
    public ResponseEntity selectStory(Integer productId, String startTime, String endTime, String origin, String userInput, Integer page, Integer size)  {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "putTime");
        Page<Story> stories = storyService.selectStory(productId, startTime, endTime, origin, userInput, pageRequest);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("customerList")
    public ResponseEntity<List> getCustomers() {
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }


    @GetMapping("download")
    public ResponseEntity<FileSystemResource> download(Integer productId, HttpServletResponse response) {
        userService.userAllowed(productId);
        //response.setContentType("application/csv");
        response.setHeader("Content-Disposition","attachment;filename=writeCSV.csv");
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + "writeCSV.csv");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        File file = new File("D:\\writeCSV.csv");
        if (file == null) {
            return null;
        }
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).
                contentType(MediaType.parseMediaType("application/octet-stream")).
                body(new FileSystemResource(file));
    }

}
