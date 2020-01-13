package fscut.manager.demo.service;

import fscut.manager.demo.entity.Product;
import java.util.List;


public interface ProductService {
    /**
     * 根据用户id展示产品列表
     * @param customerId 用户id
     * @return 产品列表
     */
    List<Product> showProductList(Integer customerId);

    /**
     * 增加产品
     * @param productName 产品名
     * @return 产品
     */
    Product save(String productName);

    /**
     * 删除产品
     * @param productId 产品id
     */
    void deleteProduct(Integer productId);
}
