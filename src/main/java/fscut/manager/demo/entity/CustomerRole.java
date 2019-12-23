package fscut.manager.demo.entity;

import fscut.manager.demo.entity.UPK.CustomerRoleUPK;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "customer_role")
public class CustomerRole {

    @EmbeddedId
    private CustomerRoleUPK customerRoleUPK;

    public CustomerRole(){
        this.customerRoleUPK = new CustomerRoleUPK();
    }
}
