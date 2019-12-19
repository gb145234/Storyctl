package fscut.manager.demo.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class GloballExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof UnauthorizedException) {
            UnauthorizedException exception = (UnauthorizedException) e;
            return ResponseEntity.status(401).body("没有权限！" + e.getMessage());
        }
        else {
            return ResponseEntity.status(202).body("token已失效" + e.getMessage());
        }
    }
}