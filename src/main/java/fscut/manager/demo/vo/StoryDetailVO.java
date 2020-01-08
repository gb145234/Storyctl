package fscut.manager.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import fscut.manager.demo.entity.Story;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StoryDetailVO {

    private Integer productId;
    private Integer storyId;
    private Boolean editable;
    private Story story;
    private List<OneTimeDetail> details;

    public StoryDetailVO(Integer productId, Integer storyId,Boolean editable){
        this.productId = productId;
        this.storyId = storyId;
        this.editable = editable;
        this.details = new ArrayList<>();
    }

    public StoryDetailVO(){

    }

    public OneTimeDetail createOne(Date date, String editName){
        return new OneTimeDetail(date,editName);
    }

    public void addOne(OneTimeDetail oneTimeDetail){
        this.details.add(oneTimeDetail);
    }


    public class OneTimeDetail{
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date date;

        private String editName;

        private List<Content> contents;

        public OneTimeDetail(Date date, String editName){
            this.date = date;
            this.editName = editName;
            this.contents = new ArrayList<>();
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getEditName() {
            return editName;
        }

        public void setEditName(String editName) {
            this.editName = editName;
        }

        public List<Content> getContents() {
            return contents;
        }

        public void setContents(List<Content> contents) {
            this.contents = contents;
        }

        public Content createOne(String attribute, String previous, String modified){
            return new Content(attribute, previous, modified);
        }

        public void addOne(Content content){
            this.contents.add(content);
        }

    }

    public class Content{
        private String attribute;
        private String previous;
        private String modified;

        public Content(String attribute, String previous, String modified){
            this.attribute = attribute;
            this.previous = previous;
            this.modified = modified;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }
    }
}
