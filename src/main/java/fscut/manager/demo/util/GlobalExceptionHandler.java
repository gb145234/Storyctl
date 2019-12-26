package fscut.manager.demo.util;

import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedHandler(UnauthorizedException e){
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(value = CustomerAlreadyExitsException.class)
    public ResponseEntity<String> customerAlreadyExitsHandler(CustomerAlreadyExitsException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(value = CustomerNotExitsException.class)
    public ResponseEntity<String> customerNotExitsHandler(CustomerNotExitsException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }
}
