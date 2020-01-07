package fscut.manager.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.dto.CustomerListDTO;
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
import fscut.manager.demo.vo.StoryDetailVO;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(designId));
        }
        if (devId != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(devId));
        }
        if (testId != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(testId));
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
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(designId));
        }
        if (devId != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(devId));
        }
        if (testId != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(testId));
        }

        return ResponseEntity.ok(updatedStory);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Page<Story>> showProductStories(@PathVariable("id") Integer id, Integer page, Integer size) {
        userService.userAllowed(id);

        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        Sort.Direction sort = Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, sort, "putTime");
        Page<Story> storyPage = storyService.getStoriesByProductId(id, user.getUserId(), pageRequest);

        return ResponseEntity.ok(storyPage);
    }

    @GetMapping("Story")
    public ResponseEntity<StoryDetailVO> showStoryInfo(Integer productId, Integer storyId, Integer edition){
        userService.userAllowed(productId);

        StoryUPK storyUPK = new StoryUPK(productId, storyId, edition);
        StoryDetailVO storyDetailVO = storyService.getStoryInfo(storyUPK);
        return ResponseEntity.ok(storyDetailVO);
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
    public ResponseEntity<Page<Story>> selectStory(Integer productId, String startTime, String endTime, String origin, String userInput, Integer page, Integer size, String sortByPutTime) {
        Sort.Direction sort = Sort.Direction.DESC;
        String desc = "descending";
        String asc = "ascending";
        if (desc.equals(sortByPutTime)) {
            sort = Sort.Direction.DESC;
        }
        else if (asc.equals(sortByPutTime)) {
            sort = Sort.Direction.ASC;
        }
        PageRequest pageRequest = PageRequest.of(page, size, sort, "putTime");
        Page<Story> stories = storyService.selectStory(productId, startTime, endTime, origin, userInput, pageRequest);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("findStory")
    public ResponseEntity<List<Story>> findStoryById(Integer productId, Integer storyId) {
        List<Story> storyList = storyService.getStoryByStoryId(productId, storyId);
        return ResponseEntity.ok(storyList);
    }

    @JsonView(Customer.SimpleView.class)
    @GetMapping("customerList")
    public ResponseEntity<CustomerListDTO> getCustomers(Integer productId) {

        CustomerListDTO customerListDTO = storyService.getCustomers(productId);

        return ResponseEntity.ok(customerListDTO);
    }

    @GetMapping("download")
    public ResponseEntity<FileSystemResource> download(Integer productId, HttpServletResponse response) throws UnsupportedEncodingException {
        userService.userAllowed(productId);
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode("writeCSV.csv", "UTF-8"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        CsvUtils.download(storyService.getStoriesByProductId(productId, user.getUserId()), response);
        File file = new File("D:\\writeCSV.csv");
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).
                contentType(MediaType.parseMediaType("application/octet-stream")).
                body(new FileSystemResource(file));
    }

}
