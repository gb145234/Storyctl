package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Resource
    private ProductService productService;

    @Test
    public void testShowProductList() {
        List<Product> products = productService.showProductList();
        Assert.assertNotEquals(0, products.size());
    }

    @Test
    public void testSave() {
        Product product = productService.save("产品10");
        Assert.assertNotEquals(null, product);
    }

    @Test
    public void testDelete() {
        productService.deleteProduct(12);
    }

}