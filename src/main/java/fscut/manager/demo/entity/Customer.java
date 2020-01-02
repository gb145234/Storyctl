package fscut.manager.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "customer")
public class Customer {

    public interface SimpleView{};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(SimpleView.class)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @JsonView(SimpleView.class)
    @Column(name = "realname", nullable = false)
    private String realName;

    @Column(name = "product_id", nullable = true)
    private Integer productId;

    public Customer() {
    }

    public Customer(Integer id, String username, String password, String realName, Integer productId){
        this.id = id;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.productId = productId;
    }

}
