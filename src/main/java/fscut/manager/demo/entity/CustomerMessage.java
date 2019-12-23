package fscut.manager.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "customer_message")
public class CustomerMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer messageId;

    private Integer customerId;

    private Boolean checked;

}
