package fscut.manager.demo.controller;


import fscut.manager.demo.entity.Product;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.ProductService;
import fscut.manager.demo.vo.CustomerAuthVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CustomerService customerService;

    @PostMapping("create")
    public ResponseEntity create(String productName) {
        Product product = productService.save(productName);
        List<String> admins = customerService.getAdmins();
        for (String admin : admins) {
            CustomerAuthVO customerAuthVO = new CustomerAuthVO();
            customerAuthVO.setProductId(product.getId());
            customerAuthVO.setRoleName("系统管理员");
            customerAuthVO.setUsername(admin);
            customerService.addToProduct(customerAuthVO);
        }
        return ResponseEntity.ok(product);
    }


    @DeleteMapping("delete")
    public ResponseEntity delete(Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("delete successfully");
    }
}
