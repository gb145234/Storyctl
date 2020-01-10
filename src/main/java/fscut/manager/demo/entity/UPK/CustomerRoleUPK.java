package fscut.manager.demo.entity.UPK;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class CustomerRoleUPK implements Serializable{

    private Integer customerId;

    private Integer roleId;

    private Integer productId;

    public CustomerRoleUPK(Integer customerId, Integer roleId, Integer productId) {
        this.customerId = customerId;
        this.roleId = roleId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerRoleUPK that = (CustomerRoleUPK) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, roleId, productId);
    }

    public CustomerRoleUPK(){}

}
