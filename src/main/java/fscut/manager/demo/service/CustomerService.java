package fscut.manager.demo.service;

import fscut.manager.demo.dto.CustomerDTO;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.vo.CustomerAuthVO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerRole addToProduct(CustomerAuthVO customerAuthVO);

    List<Customer> getCustomerListByProductId(Integer productId);

    Integer deleteFromProduct(Integer customerId, Integer productId);

    void deleteCustomer(String username) throws CustomerNotExitsException;

    void assignRole(CustomerRole customerRole);

    void deleteRole(CustomerRole customerRole);

    String getUsernameById(Integer userId);

    String getRealnameById(Integer userId);

    Integer getIdByUsername(String username);

    List<Customer> getCustomers();

    String getRoleCodeByUserId(Integer userId);

    Optional<Customer> createCustomer(CustomerDTO customerDTO) throws CustomerAlreadyExitsException;

    Integer updateCustomer(String password, String username);

    List<String> getAdmins();
}
