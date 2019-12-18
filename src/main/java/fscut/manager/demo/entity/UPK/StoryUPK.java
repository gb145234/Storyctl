package fscut.manager.demo.entity.UPK;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class StoryUPK implements Serializable{

    private Integer productId;

    private Integer storyId;

    private Integer edition;

    public StoryUPK() {
    }

    public StoryUPK(Integer productId, Integer storyId, Integer edition) {
        this.productId = productId;
        this.storyId = storyId;
        this.edition = edition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryUPK storyUPK = (StoryUPK) o;

        if (!productId.equals(storyUPK.productId)) return false;
        if (!storyId.equals(storyUPK.storyId)) return false;
        return edition.equals(storyUPK.edition);
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + storyId.hashCode();
        result = 31 * result + edition.hashCode();
        return result;
    }
}
