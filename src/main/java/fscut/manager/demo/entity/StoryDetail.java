package fscut.manager.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "story_detail")
public class StoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    private Integer storyId;

    private Date editTime;

    private String editName;

    private String attribute;

    private String previous;

    private String modified;
}
