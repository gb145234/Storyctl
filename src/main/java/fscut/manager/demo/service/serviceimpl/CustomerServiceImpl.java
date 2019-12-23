package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dao.CustomerRoleRepository;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.vo.CustomerAuthVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRoleRepository customerRoleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void addToProduct(CustomerAuthVO customerAuthVO){
        CustomerRole customerRole = new CustomerRole();

        customerRole.getCustomerRoleUPK().setProductId(customerAuthVO.getProductId());
        customerRole.getCustomerRoleUPK().setCustomerId(customerRepository.getIdByRealName(customerAuthVO.getRealName()));
        customerRole.getCustomerRoleUPK().setRoleId(customerRoleRepository.getRoleIdByRoleName(customerAuthVO.getRoleName()));
        customerRoleRepository.save(customerRole);
    }

    @Override
    public List<Customer> getCustomerList(){
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getCustomerListByProductId(Integer productId){
        return customerRepository.findCustomersByProductId(productId);
    }

    @Override
    public void deleteFromProduct(Integer customerId, Integer productId){
        customerRoleRepository.deleteFromProduct(customerId, productId);
    }

    @Override
    public Customer addCustomer(Customer customer) throws CustomerAlreadyExitsException{
        customer.setProductId(0);
        if(customerRepository.findCustomerByUsername(customer.getUsername()) != null) {
            throw new CustomerAlreadyExitsException("customer exits");
        }

        return customerRepository.save(customer);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteCustomer(String username) throws CustomerNotExitsException{
        if(customerRepository.findCustomerByUsername(username) == null) {
            throw new CustomerNotExitsException("customer not exits");
        }

        Integer customerId = customerRepository.getIdByUsername(username);
        customerRepository.deleteCustomerByUsername(username);
        customerRoleRepository.deleteRoleByCustomerId(customerId);
    }

    @Override
    public void assignRole(CustomerRole customerRole){
        customerRoleRepository.save(customerRole);
    }

    @Override
    public void deleteRole(CustomerRole customerRole){
        customerRoleRepository.delete(customerRole);
    }

    @Override
    public String getUsernameById(Integer userId) {
        if(userId == null){
            return null;
        }
        return customerRepository.getUsernameById(userId);
    }

    @Override
    public String getRealnameById(Integer userId) {
        if(userId == null){
            return null;
        }
        return customerRepository.findRealNameByCustomerId(userId);
    }

    @Override
    public Integer getIdByUsername(String username) {
        return customerRepository.getIdByUsername(username);
    }
    @Override
    public List getCustomers() {
        return customerRepository.findIdAndRealName();
    }

}
