package fscut.manager.demo.entity;

import fscut.manager.demo.entity.UPK.StoryUPK;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "story_edition")
public class StoryEdition {

    @EmbeddedId
    private StoryUPK storyUPK;

    @Column(name = "edit_id", nullable = false)
    private Integer editId;

    public StoryEdition() {
    }

    public StoryUPK getStoryUPK() {
        return storyUPK;
    }

    public void setStoryUPK(StoryUPK storyUPK) {
        this.storyUPK = storyUPK;
    }

    public Integer getEditId() {
        return editId;
    }

    public void setEditId(Integer editId) {
        this.editId = editId;
    }
}
