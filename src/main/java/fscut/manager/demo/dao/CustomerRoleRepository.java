package fscut.manager.demo.dao;

import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.entity.UPK.CustomerRoleUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CustomerRoleRepository extends JpaRepository<CustomerRole, CustomerRoleUPK> {

    @Query(value = "select id from role where role_name = ?1", nativeQuery = true)
    Integer getRoleIdByRoleName( String roleName);

    @Modifying
    @Transactional
    @Query(value = "delete from customer_role where customer_id = ?1 and product_id = ?2", nativeQuery = true)
    void deleteFromProduct(Integer customerId, Integer productId);

    @Modifying
    @Transactional
    @Query(value = "delete from customer_role where customer_id = ?1", nativeQuery = true)
    void deleteRoleByCustomerId(Integer customerId);
}
