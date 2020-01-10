package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.ProductRepository;
import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

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
    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }
}
