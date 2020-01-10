package fscut.manager.demo.aspect;


import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.vo.StoryVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private CustomerRepository customerRepository;

    @Pointcut("execution(* fscut.manager.demo.controller.StoryController.*(..))")
    public void auth(){}

    @Before("auth()")
    public void doAuth(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Subject subject = SecurityUtils.getSubject();
        UserDto userDto = (UserDto) subject.getPrincipal();
        Integer userId = userDto.getUserId();
        Integer productId = null;
        if(args[0] instanceof StoryVO){
            StoryVO tmp = (StoryVO)args[0];
            productId = tmp.getStoryUPK().getProductId();
        }
        if(args[0] instanceof StoryUPK){
            StoryUPK tmp = (StoryUPK)args[0];
            productId = tmp.getProductId();
        }
        if(args[0] instanceof Integer){
            productId = (Integer)args[0];
        }
        System.out.println(userId);
        System.out.println(productId);
        //if(customerRepository.findRoleByCustomerIdAndProductId(userId, productId) == null){
        //    throw new UnauthorizedException();
        //}
    }

}
