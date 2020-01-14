package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRoleRepository;
import fscut.manager.demo.dao.ProductRepository;
import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CustomerRoleRepository customerRoleRepository;

    @Override
    public List<Product> showProductList(Integer customerId) {
        return productRepository.findProductByCustomerId(customerId);
    }

    @Override
    public Product save(String productName) {
        Product product = new Product();
        product.setProductName(productName);
        product.setId(productRepository.findLastedProductId() + 1);
        return productRepository.save(product);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Integer deleteProduct(Integer productId) {
        Integer res = productRepository.deleteByProductId(productId);
        if (res == 1) {
            customerRoleRepository.deleteByProductId(productId);
        }
        return res;
    }
}
