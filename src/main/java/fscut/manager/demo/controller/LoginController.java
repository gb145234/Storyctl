package fscut.manager.demo.controller;

import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.ProductService;
import fscut.manager.demo.service.serviceimpl.UserService;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "首页接口", tags = {"首页接口"})
@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    @Resource
    private CustomerService customerService;

    @ApiOperation(value = "用户登录",notes = "用户登录返回头里含有token")
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserDto loginInfo, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(loginInfo.getUsername(), loginInfo.getPassword());
            subject.login(token);

            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUsername());
            response.setHeader("token", newToken);

            Integer userId = customerService.getIdByUsername(user.getUsername());
            String roleCode = customerService.getRoleCodeByUserId(userId);
            return ResponseEntity.ok(roleCode);
        } catch (AuthenticationException e) {
            logger.error("User {} login fail, Reason:{}", loginInfo.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
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


    @ApiOperation(value = "获取产品列表",notes = "根据用户所属产品返回对应产品列表")
    @GetMapping("list")
    public ResponseEntity<List<Product>> showProductList() {
        Subject subject = SecurityUtils.getSubject();
        UserDto userDto = (UserDto) subject.getPrincipal();
        List<Product> products = productService.showProductList(userDto.getUserId());
        return ResponseEntity.ok(products);
    }
}
