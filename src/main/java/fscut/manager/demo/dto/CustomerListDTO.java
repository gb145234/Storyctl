package fscut.manager.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.entity.Customer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerListDTO implements Serializable {

    @JsonView(Customer.SimpleView.class)
    private List<Customer> developer;

    @JsonView(Customer.SimpleView.class)
    private List<Customer> designer;

    @JsonView(Customer.SimpleView.class)
    private List<Customer> tester;

}
