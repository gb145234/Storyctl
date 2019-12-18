package fscut.manager.demo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户对象
 */
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = -9077975168976887742L;

    private String username;
    private String password;
    private String encryptPwd;
    private Integer userId;
    private String salt;
    private List<Integer> productIds;
    private List<String> roles;

}
