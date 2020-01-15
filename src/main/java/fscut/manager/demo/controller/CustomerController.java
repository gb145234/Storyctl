package fscut.manager.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.dto.CustomerDTO;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.entity.UPK.CustomerRoleUPK;
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
    public ResponseEntity<Integer> deleteCustomer(String username) throws CustomerNotExitsException {
        Integer res = customerService.deleteCustomer(username);
        if (res != 1) {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
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
    @RequiresRoles(value={"admin","manager"}, logical = Logical.OR)
    public ResponseEntity<List<Customer>> getCustomerListByProductId(@PathVariable("id") Integer productId){
        List<Customer> customerList = customerService.getCustomerListByProductId(productId);
        return ResponseEntity.ok(customerList);
    }

    @PostMapping("addToProduct")
    @RequiresRoles(value={"admin","manager"}, logical = Logical.OR)
    public ResponseEntity addToProduct(@RequestBody CustomerAuthVO customerAuthVO) {
        CustomerRoleUPK customerRoleUPK = customerService.getCustomerRoleByCustomerIdAndProductId(customerAuthVO);
        if (customerRoleUPK == null) {
            CustomerRole customerRole1 = customerService.addToProduct(customerAuthVO);
            return ResponseEntity.ok(customerRole1);
        }
        else {
            Integer res = customerService.updateCustomerRole(customerAuthVO);
            if (res != 1) {
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(res);
        }
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
        CustomerAuthVO customerAuthVO = new CustomerAuthVO();
        customerAuthVO.setUsername(customerDTO.getUsername());
        customerAuthVO.setRoleName("普通用户");
        customerAuthVO.setProductId(customerDTO.getProductId());
        customerService.addToProduct(customerAuthVO);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("updatePassword")
    public ResponseEntity<String> updatePassword(String oldPassword, String newPassword, String username) {
        Integer res = customerService.updateCustomerPassword(oldPassword, newPassword, username);
        if (res == -2) {
            return new ResponseEntity<>("原密码不能为空！", HttpStatus.BAD_REQUEST);
        }
        if (res == -1) {
            return new ResponseEntity<>("与原密码不相符！", HttpStatus.BAD_REQUEST);
        }
        if (res == 0) {
            return new ResponseEntity<>("修改失败！", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("修改成功");
    }

    @PostMapping("updateRealName")
    public ResponseEntity<Integer> updateRealName(String newName, String username) {
        Integer res = customerService.updateCustomerRealName(newName, username);
        if (res != 1) {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
    }
}
