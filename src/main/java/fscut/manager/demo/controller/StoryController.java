package fscut.manager.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.config.IpConfiguration;
import fscut.manager.demo.dto.CustomerListDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.service.serviceimpl.UserService;
import fscut.manager.demo.util.CsvUtils;
import fscut.manager.demo.vo.StoryDetailVO;
import fscut.manager.demo.vo.StoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Api(value = "需求controller",tags = {"用户操作接口"})
@RequestMapping("story")
@Slf4j
public class StoryController {

    @Resource
    private StoryService storyService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private IpConfiguration ipConfiguration;

    @JsonView(Story.StorySimpleView.class)
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
        messageService.sendMessage(newStory, message);

        return ResponseEntity.ok(newStory);
    }

    @JsonView(Story.StorySimpleView.class)
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
        messageService.sendMessage(updatedStory,message);

        return ResponseEntity.ok(updatedStory);
    }

    @ApiOperation(value = "获取产品所有需求",notes = "验证用户权限")
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

    @JsonView(Story.StorySimpleView.class)
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
        if (res != 1) {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("selectStory")
    public ResponseEntity<Page<Story>> selectStory(Integer productId, String startTime, String endTime, String origin, String userInput, Integer page, Integer size, String sortByPutTime) {
        userService.userAllowed(productId);

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

    @JsonView({Story.StorySimpleView.class})
    @GetMapping("findStory")
    public ResponseEntity<List<Story>> findStoryById(Integer productId, Integer storyId) {
        userService.userAllowed(productId);

        List<Story> storyList = storyService.getStoryByStoryId(productId, storyId);
        return ResponseEntity.ok(storyList);
    }

    @JsonView(Customer.SimpleView.class)
    @GetMapping("customerList")
    public ResponseEntity<CustomerListDTO> getCustomers(Integer productId) {
        userService.userAllowed(productId);

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
        String pathName = "D:\\writeCSV.csv";
        File file = new File(pathName);
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).
                contentType(MediaType.parseMediaType("application/octet-stream")).
                body(new FileSystemResource(file));
    }

    @RequestMapping("/upload")
    public ResponseEntity image(MultipartFile file) {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.info(e.getMessage());
        }
        if (file.isEmpty()) {
            return new ResponseEntity<>("文件不可为空", HttpStatus.BAD_REQUEST);
        }
        String filaName = file.getOriginalFilename();
        filaName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filaName;
        String dirPath = System.getProperty("user.dir") + "\\src\\main\\resources\\public\\upload\\";
        String hostAddress = null;
        if (address != null) {
            hostAddress = address.getHostAddress();
        }
        if (hostAddress == null) {
            return new ResponseEntity<>("ip地址不能为空！", HttpStatus.BAD_REQUEST);
        }
        String url = "http://" + hostAddress + ":" + ipConfiguration.getPort() + "/picture/";
        File filePath = new File(dirPath);
        if(!filePath.exists()) {
            boolean mkdir = filePath.mkdir();
            if (!mkdir) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        try {
            file.transferTo(new File(dirPath + filaName));
            log.info(url + filaName);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        }
        String res = url + filaName;
        return ResponseEntity.ok(res);
    }

}
