package fscut.manager.demo.controller;

import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.service.ProductService;
import fscut.manager.demo.service.serviceimpl.UserService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ProductService productService;

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody UserDto loginInfo, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(loginInfo.getUsername(), loginInfo.getPassword());
            subject.login(token);

            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUsername());
            response.setHeader("token", newToken);

            WebSocketServer.sendInfo("您共有" + messageService.getUnreadMessageNum(user.getUsername()) + "条消息未读", user.getUsername());
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            logger.error("User {} login fail, Reason:{}", loginInfo.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 退出登录
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            UserDto user = (UserDto) subject.getPrincipals().getPrimaryPrincipal();
            userService.deleteLoginInfo(user.getUsername());
        }
        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    public ResponseEntity<List<Product>> showProductList() {
        List<Product> products = productService.showProductList();
        return ResponseEntity.ok(products);
    }
}
