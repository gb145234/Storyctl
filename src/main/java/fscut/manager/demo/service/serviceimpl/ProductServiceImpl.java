package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.ProductRepository;
import fscut.manager.demo.entity.Product;
import fscut.manager.demo.exception.CustomerNoAuthorityException;
import fscut.manager.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> showProductList() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }

    @Override
    public Product save(String productName) {
        Product product = new Product();
        product.setProductName(productName);
        product.setId(productRepository.findLastedProductId() + 1);
        Product newProduct = productRepository.save(product);
        return newProduct;
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }
}
