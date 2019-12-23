package fscut.manager.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.vo.CustomerAuthVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("addCustomer")
    @RequiresRoles("admin")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) throws CustomerAlreadyExitsException {
        Customer result = customerService.addCustomer(customer);
        return ResponseEntity.ok(result);
    }

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
        List<Customer> customerList = customerService.getCustomerList();
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
    @RequiresRoles("manager")
    public ResponseEntity<String> addToProduct(@RequestBody CustomerAuthVO customerAuthVO){
        customerService.addToProduct(customerAuthVO);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("deleteFromProduct")
    @RequiresRoles("manager")
    public ResponseEntity<Void> deleteFromProduct(Integer customerId, Integer productId){
        customerService.deleteFromProduct(customerId,productId);
        return ResponseEntity.ok(null);
    }



}
