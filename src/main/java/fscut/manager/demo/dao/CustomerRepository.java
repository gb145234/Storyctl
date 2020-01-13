package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

    /**
     * 根据用户名查找用户id
     * @param username 用户名
     * @return 用户id
     */
    @Query(value = "select id from customer where username = ?1", nativeQuery = true)
    Integer getIdByUsername(String username);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户
     */
    Customer findCustomerByUsername(String username);

    /**
     * 根据用户id查找产品id列表
     * @param customerId 用户id
     * @return 产品id列表
     */
    @Query(value = "select DISTINCT product_id from customer_role where customer_id = ?1 order by product_id ", nativeQuery = true)
    List<Integer> findProductIdsByCustomerId(Integer customerId);

    /**
     * 根据用户id和产品id查找角色id
     * @param customerId 用户id
     * @param productId 产品id
     * @return 角色id
     */
    @Query(value = "select role_id from customer_role where customer_id = ?1 and product_id = ?2", nativeQuery = true)
    Integer findRoleByCustomerIdAndProductId(Integer customerId, Integer productId);

    /**
     * 根据产品id查找用户列表
     * @param productId 产品id
     * @return 用户列表
     */
    @Query("select new fscut.manager.demo.entity.Customer(c.id,c.username,c.password,c.realName,c.productId) from Customer c, CustomerRole cr where cr.customerRoleUPK.productId = :productId and cr.customerRoleUPK.customerId = c.id")
    List<Customer> findCustomersByProductId(@Param("productId") Integer productId);

    /**
     * 根据产品id和角色id查找用户列表
     * @param productId 产品id
     * @param roleId 角色id
     * @return 用户列表
     */
    @Query("select new fscut.manager.demo.entity.Customer(c.id,c.username,c.password,c.realName,c.productId) from Customer c, CustomerRole cr where cr.customerRoleUPK.productId = :productId and cr.customerRoleUPK.roleId = :roleId and cr.customerRoleUPK.customerId = c.id")
    List<Customer> getCustomersByProductIdAndRole(@Param("productId") Integer productId, @Param("roleId") Integer roleId);

    /**
     * 根据用户id查找角色名
     * @param userId 用户id
     * @return 角色名
     */
    @Query(value = "select DISTINCT role_code from customer_role as cr left join role on cr.role_id = role.id where customer_id = ?1", nativeQuery = true)
    List<String> findRolesByCustomerId(Integer userId);

    /**
     * 根据用户id查找角色名
     * @param userId 用户id
     * @return 角色名
     */
    @Query(value = "select DISTINCT role_code from customer_role as cr left join role on cr.role_id = role.id where customer_id = ?1", nativeQuery = true)
    String findRoleCodeByUserId(Integer userId);

    /**
     * 根据用户名删除用户
     * @param username 用户名
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer where username = ?1", nativeQuery = true)
    void deleteCustomerByUsername(String username);

    /**
     * 根据用户id查找真实姓名
     * @param id 用户id
     * @return 真实姓名
     */
    @Query(value = "select realname from customer where id = ?1", nativeQuery = true)
    String findRealNameByCustomerId(Integer id);

    /**
     * 根据用户id查找用户名
     * @param userId 用户id
     * @return 用户名
     */
    @Query(value = "select username from customer where id = ?1", nativeQuery = true)
    String getUsernameById(Integer userId);

    /**
     * 修改用户密码
     * @param password 密码
     * @param customerId 用户id
     * @return 修改条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update customer set password = ?1 where id = ?2", nativeQuery = true)
    Integer updateCustomerPassword(String password, Integer customerId);


}
