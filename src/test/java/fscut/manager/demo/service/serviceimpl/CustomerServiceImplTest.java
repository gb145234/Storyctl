package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.service.CustomerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplTest {

    private CustomerService customerService;

    @Test
    public void getCustomers() throws Exception {
        List<Customer> customerList = customerService.getCustomers();
        System.out.println(customerList.size());
        Assert.assertNotEquals(0, customerList.size());
    }

}