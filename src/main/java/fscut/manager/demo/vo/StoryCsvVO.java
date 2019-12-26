package fscut.manager.demo.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class StoryCsvVO {

    private String storyName;
    private String origin;
    private Date putTime;

    private Integer storyStatus;

    private String description;
    private String conclusion;

    private String editName;
    private String designName;
    private String devName;
    private String testName;

    private Date testTime;
    private java.util.Date updateTime;
}
