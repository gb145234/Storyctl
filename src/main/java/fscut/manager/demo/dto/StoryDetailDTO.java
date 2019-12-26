package fscut.manager.demo.dto;

import fscut.manager.demo.entity.Story;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class StoryDetailDTO implements Serializable{

    private static final long serialVersionUID = 4524324257801434551L;

    private Story story;

    private List<Story> storyList;

    public StoryDetailDTO(){
        storyList = new ArrayList<>();
    }


}
