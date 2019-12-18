package fscut.manager.demo.dto;

import com.sun.xml.internal.ws.developer.Serialization;
import fscut.manager.demo.entity.Story;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class StoryDetailDTO implements Serializable{

    private Story story;

    private List<Story> storyList;

    public StoryDetailDTO(){
        storyList = new ArrayList<Story>();
    }


}
