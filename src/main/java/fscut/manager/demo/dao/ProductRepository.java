package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * 查询所有产品
     * @param pageable 分页
     * @return 产品分页
     */
    @Override
    Page<Product> findAll(Pageable pageable);

    /**
     * 创建产品
     * @param product 产品
     * @return 新增产品
     */
    @Override
    Product save(Product product);

    /**
     * 删除产品
     * @param productId 产品id
     */
    @Override
    void deleteById(Integer productId);

    /**
     * 查找上一个产品id
     * @return id
     */
    @Query(value = "select id from Product order by id DESC limit 1", nativeQuery = true)
    Integer findLastedProductId();

    @Query("select new fscut.manager.demo.entity.Product(p.id, p.productName) from Product p, CustomerRole cr where p.id = cr.customerRoleUPK.productId and cr.customerRoleUPK.customerId = :customerId")
    List<Product> findProductByCustomerId(@Param("customerId") Integer customerId);
}
