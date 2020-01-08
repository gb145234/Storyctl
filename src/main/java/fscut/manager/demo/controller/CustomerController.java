package fscut.manager.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.dto.CustomerDTO;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.vo.CustomerAuthVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @DeleteMapping("deleteCustomer")
    @RequiresRoles("admin")
    public ResponseEntity<Void> deleteCustomer(String username) throws CustomerNotExitsException {
        customerService.deleteCustomer(username);
        return ResponseEntity.ok(null);
    }

    @JsonView({Customer.SimpleView.class})
    @GetMapping("customerList")
    @RequiresRoles(value={"admin","manager"}, logical = Logical.OR)
    public ResponseEntity<List<Customer>> getCustomerList(){
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }

    @JsonView({Customer.SimpleView.class})
    @GetMapping("customerList/{id}")
    @RequiresRoles(value={"admin","manager"},logical = Logical.OR)
    public ResponseEntity<List<Customer>> getCustomerListByProductId(@PathVariable("id") Integer productId){
        List<Customer> customerList = customerService.getCustomerListByProductId(productId);
        return ResponseEntity.ok(customerList);
    }


    @RequiresRoles("admin")
    @PostMapping("assignRole")
    public ResponseEntity<Void> assignRole(@RequestBody CustomerRole customerRole){
        customerService.assignRole(customerRole);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("deleteRole")
    @RequiresRoles("admin")
    public ResponseEntity<Void> deleteRole(@RequestBody CustomerRole customerRole){
        customerService.deleteRole(customerRole);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addToProduct")
    @RequiresRoles(value={"admin","manager"}, logical = Logical.OR)
    public ResponseEntity<CustomerRole> addToProduct(@RequestBody CustomerAuthVO customerAuthVO){
        CustomerRole customerRole = customerService.addToProduct(customerAuthVO);
        return ResponseEntity.ok(customerRole);
    }

    @DeleteMapping("deleteFromProduct")
    @RequiresRoles("manager")
    public ResponseEntity<Integer> deleteFromProduct(Integer customerId, Integer productId){
        Integer res = customerService.deleteFromProduct(customerId, productId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("createCustomer")
    @RequiresRoles("admin")
    public ResponseEntity createCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerAlreadyExitsException {
        Optional<Customer> optional = customerService.createCustomer(customerDTO);
        Customer customer = null;
        if (optional.isPresent()) {
            customer = optional.get();
        }
        if (customer == null) {
            return ResponseEntity.ok("为空！");
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping("updateCustomer")
    @RequiresRoles("admin")
    public ResponseEntity<Integer> updateCustomer(String password, String username) {
        Integer res = customerService.updateCustomer(password, username);
        if (res != 1) {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
    }

}
