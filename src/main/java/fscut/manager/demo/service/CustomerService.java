package fscut.manager.demo.service;

import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.vo.CustomerAuthVO;

import java.util.List;

public interface CustomerService {

    void addToProduct(CustomerAuthVO customerAuthVO);

    List<Customer> getCustomerList();

    List<Customer> getCustomerListByProductId(Integer productId);

    void deleteFromProduct(Integer customerId, Integer productId);

    Customer addCustomer(Customer customer) throws CustomerAlreadyExitsException;

    void deleteCustomer(String username) throws CustomerNotExitsException;

    void assignRole(CustomerRole customerRole);

    void deleteRole(CustomerRole customerRole);

    String getUsernameById(Integer userId);

    String getRealnameById(Integer userId);

    Integer getIdByUsername(String username);

    List<Customer> getCustomers();
}
