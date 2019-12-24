package fscut.manager.demo.controller;

import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.exception.CustomerNoAuthorityException;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.service.serviceimpl.UserService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import fscut.manager.demo.vo.StoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

        messageService.addMessage(newStory,"新建");

        WebSocketServer.sendInfo(messageService.getUnreadMessageNum(newStory.getDesignId()),
                    customerService.getUsernameById(newStory.getDesignId()));

        return ResponseEntity.ok(newStory);
    }

    @PostMapping("editStory")
    public ResponseEntity editStory(@RequestBody StoryVO storyVO){
        userService.userAllowed(storyVO.getStoryUPK().getProductId());

        Story story = storyService.convertStoryVO2Story(storyVO);
        Optional<Story> optional = storyService.editStory(story);
        Story newStory = null;
        if (optional.isPresent()) {
            newStory = optional.get();
        }
        if (newStory == null) {
            return ResponseEntity.ok("为空！");
        }
        return ResponseEntity.ok(newStory);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Page<Story>> showProductStories(@PathVariable("id") Integer id, Integer page, Integer size) throws CustomerNoAuthorityException {
        userService.userAllowed(id);

        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size);
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
    public ResponseEntity<String> deleteStory(@RequestBody StoryUPK storyUPK){
        userService.userAllowed(storyUPK.getProductId());

        storyService.deleteStory(storyUPK);
        return ResponseEntity.ok("Delete successfully!");
    }

    @PostMapping("selectStory")
    public ResponseEntity selectStory(Integer productId, String startTime, String endTime, String origin, String userInput, Integer page, Integer size)  {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Story> stories = storyService.selectStory(productId, startTime, endTime, origin, userInput, pageRequest);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("customerList")
    public ResponseEntity<List> getCustomers() {
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }


    @GetMapping("download")
    public void download(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment;file=writeCSV.csv");
        //CsvUtils.download(storyService.getStoriesByProductId(1,1));
    }


}
