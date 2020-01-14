package fscut.manager.demo.dao;

import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.entity.UPK.CustomerRoleUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerRoleRepository extends JpaRepository<CustomerRole, CustomerRoleUPK> {

    /**
     * 根据角色名查找角色id
     * @param roleName 角色名
     * @return 角色id
     */
    @Query(value = "select id from role where role_name = ?1", nativeQuery = true)
    Integer getRoleIdByRoleName( String roleName);

    /**
     * 根据用户id和产品id删除用户的产品
     * @param customerId 用户id
     * @param productId 产品id
     * @return 删除条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_role where customer_id = ?1 and product_id = ?2", nativeQuery = true)
    Integer deleteFromProduct(Integer customerId, Integer productId);

    /**
     * 根据用户id删除用户角色
     * @param customerId 用户id
     * @return 删除条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_role where customer_id = ?1", nativeQuery = true)
    Integer deleteRoleByCustomerId(Integer customerId);

    /**
     * 删除某个产品的所有用户角色
     * @param productId 产品id
     * @return 删除条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_role where product_id = ?1", nativeQuery = true)
    Integer deleteByProductId(Integer productId);

    /**
     * 查找所有管理员
     * @return 用户id列表
     */
    @Query(value = "select distinct customer_id from customer_role where role_id = 6", nativeQuery = true)
    List<Integer> findAllAdmins();

    /**
     * 更新用户对于某个产品角色
     * @param roleId 角色id
     * @param customerId 用户id
     * @param productId 产品id
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update CustomerRole set role_id = ?1 where customer_id = ?2 and product_id = ?3")
    Integer updateCustomerRole(Integer roleId, Integer customerId, Integer productId);

    /**
     * 根据用户id和产品id查询用户角色主键
     * @param customerId 用户id
     * @param productId 产品id
     * @return 用户角色主键
     */
    @Query(value = "select new fscut.manager.demo.entity.UPK.CustomerRoleUPK(cr.customerRoleUPK.customerId,cr.customerRoleUPK.roleId,cr.customerRoleUPK.productId) from CustomerRole cr where cr.customerRoleUPK.customerId = :customerId and cr.customerRoleUPK.productId = :productId")
    CustomerRoleUPK findByCustomerIdAndProductId(@Param("customerId") Integer customerId, @Param("productId") Integer productId);
}
