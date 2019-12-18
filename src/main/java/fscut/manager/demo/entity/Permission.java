package fscut.manager.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "permission")
public class Permission {

    @Id
    private Integer id;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @Column(name = "description")
    private String description;
}
