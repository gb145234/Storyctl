package fscut.manager.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = -8785279883880784700L;

    private String username;
    private String password;
    private String realName;
    private Integer productId;
}
