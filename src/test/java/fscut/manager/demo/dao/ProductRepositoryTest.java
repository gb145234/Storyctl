package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindAll() {
        List<Product> all = productRepository.findAll();
        System.out.println(all.size());
        Assert.assertNotEquals(0, all.size());
    }

    @Test
    public void testFindLastedProductId() {
        Integer lastedProductId = productRepository.findLastedProductId();
        Assert.assertNotEquals(java.util.Optional.of(0), lastedProductId);
    }

    @Test
    public void testDelete() {
        productRepository.deleteById(7);
    }

}