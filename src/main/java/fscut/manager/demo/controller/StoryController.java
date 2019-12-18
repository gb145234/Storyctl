package fscut.manager.demo.controller;

import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.exception.CustomerNoAuthorityException;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.ProductService;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.service.serviceimpl.UserService;
import fscut.manager.demo.vo.StoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    private ProductService productService;

    @Resource
    private CustomerService customerService;

    @PostMapping("newStory")
    public ResponseEntity<Story> newStory(@RequestBody StoryVO storyVO){
        userService.userAllowed(storyVO.getStoryUPK().getProductId());

        Story story = storyService.convertStoryVO2Story(storyVO);

        Optional<Story> optional = storyService.addStory(story);
        return ResponseEntity.ok(optional.get());
    }

    @PostMapping("editStory")
    public ResponseEntity<Story> editStory(@RequestBody StoryVO storyVO){
        userService.userAllowed(storyVO.getStoryUPK().getProductId());

        Story story = storyService.convertStoryVO2Story(storyVO);
        Optional<Story> optional = storyService.editStory(story);
        return ResponseEntity.ok(optional.get());
    }

    @GetMapping("product/{id}")
    public ResponseEntity<List<Story>> showProductStories(@PathVariable("id") Integer id) throws CustomerNoAuthorityException {
        userService.userAllowed(id);

        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        List<Story> stories = storyService.getStoriesByProductId(id, user.getUserId());

        return ResponseEntity.ok(stories);
    }

    @GetMapping("Story")
    public ResponseEntity<StoryDetailDTO> showStoryInfo(Integer productId, Integer storyId, Integer edition){
        userService.userAllowed(productId);

        StoryUPK storyUPK = new StoryUPK(productId, storyId, edition);
        StoryDetailDTO storyDetailDTO = storyService.getStoryInfo(storyUPK);
        return ResponseEntity.ok(storyDetailDTO);
    }

    @GetMapping("history")
    public ResponseEntity<List<Story>> showStoryHistory(@RequestBody StoryUPK storyUPK, Integer page, Integer size){
        userService.userAllowed(storyUPK.getProductId());

        //PageRequest pageRequest = PageRequest.of(page, size);
        List<Story> stories = storyService.getStoryHistory(storyUPK);
        return ResponseEntity.ok(stories);
    }

    @DeleteMapping("deleteStory")
    public ResponseEntity<String> deleteStory(@RequestBody StoryUPK storyUPK){
        userService.userAllowed(storyUPK.getProductId());

        storyService.deleteStory(storyUPK);
        return ResponseEntity.ok("Delete successfully!");
    }

    @GetMapping("searchStory")
    public ResponseEntity<Page<Story>> searchStory(String input, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Story> searchStoryPage = storyService.searchStory(input, pageRequest);
        return ResponseEntity.ok(searchStoryPage);
    }

    @PostMapping("selectStory")
    public ResponseEntity searchStory(Integer productId, Long startTime, Long endTime, String origin, String userInput, Integer page, Integer size)  {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Story> stories = storyService.selectStory(productId, startTime, endTime, origin, userInput, pageRequest);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("customerList")
    public ResponseEntity<List> getCustomers() {
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }


}
