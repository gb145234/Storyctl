package fscut.manager.demo.service.serviceimpl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.util.token.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户信息接口
 */
@Service
public class UserService {
	
	private static final String ENCRYPTSALT = "F12839WhsnnEV$#23b";

    private RedisTemplate<String,String> redisTemplate;

    private CustomerRepository customerRepository;

    public UserService(RedisTemplate<String,String> redisTemplate, CustomerRepository customerRepository){
        this.redisTemplate = redisTemplate;
        this.customerRepository = customerRepository;
    }

    /**
     * 保存user登录信息，返回token
     * @param
     */ 
    public String generateJwtToken(String username) {
    	String salt = JwtUtils.generateSalt();

    	redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);

    	return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }
    
    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    public UserDto getJwtTokenInfo(String username) {

		String salt = redisTemplate.opsForValue().get("token:"+username);

    	UserDto user = getUserInfo(username);
    	user.setSalt(salt);
    	return user;
    }

    /**
     * 清除token信息
     * @param 
     * @param
     */
    public void deleteLoginInfo(String username) {

    	 redisTemplate.delete("token:"+username);

    }
    
    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    //todo
    public UserDto getUserInfo(String userName) {
    	UserDto user = new UserDto();
    	Customer customer = customerRepository.findCustomerByUsername(userName);
    	List<Integer> productIds = customerRepository.findProductIdsByCustomerId(customer.getId());
    	user.setRoles(getUserRoles(customer.getId()));
    	user.setUserId(customer.getId());
    	user.setUsername(userName);
        user.setProductIds(productIds);
    	user.setEncryptPwd(new Sha256Hash(customer.getPassword(), ENCRYPTSALT).toHex());
    	return user;
    }


    @Cacheable(value = "role")
    public List<String> getUserRoles(Integer userId){
        return customerRepository.findRolesByCustomerId(userId);
    }

    @Cacheable(value = "role")
    public boolean isUserAllowed(Integer productId, Integer userId){
        return customerRepository.findRoleByCustomerIdAndProductId(userId, productId) != null;
    }

    /**
     * 根据token判断用户是否具有产品的权限
     * @param productId 产品Id
     * @return
     */
    public Void userAllowed(Integer productId){
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        if(!isUserAllowed(productId, user.getUserId()))
            throw new UnauthorizedException();
        return null;
    }


}
