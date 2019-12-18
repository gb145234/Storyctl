package fscut.manager.demo.entity;

import fscut.manager.demo.entity.UPK.RolePermissionUPK;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "role_permission")
public class RolePermission {

    @EmbeddedId
    private RolePermissionUPK rolePermissionUPK;

}
