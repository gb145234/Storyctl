package fscut.manager.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.enums.StoryStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "story")
@DynamicUpdate
@JsonInclude(Include.NON_NULL)
public class Story implements Serializable {

    private static final long serialVersionUID = -5246468425523820009L;

    public interface StoryListSimpleView{}

    @EmbeddedId
    private StoryUPK storyUPK;

    @Column(name = "origin")
    private String origin;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "put_time", nullable = false)
    private Date putTime;

    @Column(name = "story_name", nullable = false)
    private String storyName;

    @Column(name = "story_status", nullable = false)
    private Integer storyStatus = StoryStatusEnum.NEW.getCode();

    @Column(name = "description")
    private String description;

    @Column(name = "conclusion")
    private String conclusion;

    @Column(name = "design_id")
    private Integer designId;

    @Column(name = "dev_id")
    private Integer devId;

    @Column(name = "test_id")
    private Integer testId;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @Column(name = "test_time")
    private Date testTime;

    @Column(name = "edit_id", nullable = false)
    private Integer editId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(name = "update_time", nullable = false)
    private java.util.Date updateTime;

    public Story() {
    }

    @JsonView({Story.StoryListSimpleView.class})
    public StoryUPK getStoryUPK() {
        return storyUPK;
    }

    public void setStoryUPK(StoryUPK storyUPK) {
        this.storyUPK = storyUPK;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getPutTime() {
        return putTime;
    }

    @JsonView({Story.StoryListSimpleView.class})
    public void setPutTime(Date putTime) {
        this.putTime = putTime;
    }

    @JsonView({Story.StoryListSimpleView.class})
    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    @JsonView({Story.StoryListSimpleView.class})
    public Integer getStoryStatus() {
        return storyStatus;
    }

    public void setStoryStatus(Integer storyStatus) {
        this.storyStatus = storyStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Integer getDesignId() {
        return designId;
    }

    public void setDesignId(Integer designId) {
        this.designId = designId;
    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Integer getEditId() {
        return editId;
    }

    public void setEditId(Integer editId) {
        this.editId = editId;
    }

    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyUPK=" + storyUPK +
                ", origin='" + origin + '\'' +
                ", putTime=" + putTime +
                ", storyName='" + storyName + '\'' +
                ", storyStatus=" + storyStatus +
                ", description='" + description + '\'' +
                ", conclusion='" + conclusion + '\'' +
                ", designId=" + designId +
                ", devId=" + devId +
                ", testId=" + testId +
                ", testTime=" + testTime +
                ", editId=" + editId +
                ", updateTime=" + updateTime +
                '}';
    }
}
