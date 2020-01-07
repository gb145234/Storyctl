package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindAll() throws Exception {
        List<Customer> customerList = customerRepository.findAll();
        System.out.println(customerList.size());
        Assert.assertNotEquals(0, customerList.size());
    }

}