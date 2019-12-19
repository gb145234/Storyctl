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

    @Query(value = "select id from customer where realname = ?1", nativeQuery = true)
    Integer getIdByRealName(String realName);

    @Query(value = "select id from customer where username = ?1", nativeQuery = true)
    Integer getIdByUsername(String username);

    Customer findCustomerByUsername(String Username);

    @Query(value = "select role_code from customer as c right join customer_role as cr " +
            "on c.id = cr.customer_id left join role as r " +
            "on cr.role_id = r.id where c.id = :userId and cr.product_id in :productIds ", nativeQuery = true)
    List<String> findRolesByCustomerIdAndProductIds(@Param("userId") Integer userId, @Param("productIds") List<Integer> productIds);

    @Query(value = "select DISTINCT product_id from customer_role where customer_id = ?1 order by product_id ", nativeQuery = true)
    List<Integer> findProductIdsByCustomerId(Integer customerId);

    @Query("select new fscut.manager.demo.entity.Customer(c.id,c.username,c.password,c.realName,c.productId) from Customer c, CustomerRole cr where cr.customerRoleUPK.productId = :productId and cr.customerRoleUPK.customerId = c.id")
    List<Customer> findCustomersByProductId(@Param("productId") Integer productId);

    @Query(value = "select role_code from customer_role as cr left join role on cr.role_id = role.id where customer_id = ?1", nativeQuery = true)
    List<String> findRolesByCustomerId(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "delete from customer where username = ?1", nativeQuery = true)
    void deleteCustomerByUsername(String username);

    /**
     * 查找所有用户的id和真实姓名
     * @return 用户列表
     */
    @Query(value = "select id,realname from customer", nativeQuery = true)
    List findIdAndRealName();

}