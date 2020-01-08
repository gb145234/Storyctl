package fscut.manager.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    public Product(Integer id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public Product() {
    }
}
