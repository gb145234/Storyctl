package fscut.manager.demo.vo;

import fscut.manager.demo.entity.UPK.StoryUPK;
import lombok.Data;

import java.sql.Date;

@Data
public class StoryVO {

    private StoryUPK storyUPK;

    private String origin;

    private Date putTime;

    private String storyName;

    private Integer storyStatus;

    private String description;

    private String conclusion;

    private Integer designId;

    private Integer devId;

    private Integer testId;

    private Date testTime;

    private java.util.Date updateTime;

}
