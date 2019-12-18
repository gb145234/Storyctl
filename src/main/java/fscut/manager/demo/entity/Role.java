package fscut.manager.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "role")
public class Role {

    @Id
    private Integer id;

    @Column(name = "role_name", nullable = false)
    private String roleName;
}
