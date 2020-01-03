package fscut.manager.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import fscut.manager.demo.entity.UPK.StoryUPK;
import lombok.Data;

import java.sql.Date;

@Data
public class StoryVO {

    private StoryUPK storyUPK;

    private String origin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date putTime;

    private String storyName;

    private Integer storyStatus;

    private String description;

    private String conclusion;

    private Integer designId;

    private Integer devId;

    private Integer testId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date updateTime;

}
