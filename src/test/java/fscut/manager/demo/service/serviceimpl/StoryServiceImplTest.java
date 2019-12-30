package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.service.StoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryServiceImplTest {

    @Autowired
    private StoryService storyService;

    @Test
    public void testGetStoryByStoryNameLike() {
        List<Story> storyList = storyService.getStoryByStoryNameLike("次");
        Assert.assertNotEquals(0, storyList.size());
    }

    @Test
    public void testGetStoryByDescriptionLike() {
        List<Story> storyList = storyService.getStoryByDescriptionLike("o");
        Assert.assertNotEquals(0, storyList.size());
    }

    @Test
    public void testSelectStory() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Story> storyPage = storyService.selectStory(1, "2019-12-19", "2019-12-20", null, null, pageRequest);
        System.out.println(storyPage.getTotalElements());
        for (Story story : storyPage) {
            System.out.println(story.toString());
        }
        Assert.assertNotEquals(0, storyPage.getTotalElements());
    }

    @Test
    public void testgetStoryHistory() throws Exception {
        StoryUPK storyUPK = new StoryUPK();
        storyUPK.setProductId(1);
        storyUPK.setStoryId(33);
        List<Story> storyHistory = storyService.getStoryHistory(storyUPK);
        System.out.println(storyHistory.size());
        Assert.assertNotEquals(0, storyHistory.size());
    }

    @Test
    public void testGetStoriesByProductId() throws Exception {
        List<Story> storyList = storyService.getStoriesByProductId(1, 1);
        Assert.assertNotEquals(0, storyList.size());
    }

    //@Test
    //public void testGetStoryEditionsByProductId() throws Exception {
    //    PageRequest pageRequest = PageRequest.of(0, 10);
    //    Page<StoryUPK> page = storyService.getStoryEditionsByProductId(1, pageRequest);
    //    Assert.assertNotEquals(0, page.getTotalElements());
    //}

}