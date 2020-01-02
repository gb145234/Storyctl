package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Story;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryRepositoryTest {

    @Resource
    private StoryRepository storyRepository;

    @Resource
    private StoryEditionRepository storyEditionRepository;

    @Test
    public void testFindByStoryNameContainingAndDescriptionContaining() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Story> storyList = storyRepository.findByStoryNameContainingAndDescriptionContaining("次", "o", pageRequest);
        System.out.println(storyList.getTotalElements());
        Assert.assertNotEquals(0, pageRequest);
    }

    @Test
    public void testFindByDescriptionContains() throws Exception {
        List<Story> storyList = storyRepository.findByDescriptionContaining("o");
        Assert.assertNotEquals(0, storyList.size());
    }

    @Test
    public void testFindByNameContains() throws Exception {
        List<Story> storyList = storyRepository.findByStoryNameContaining("次");
        Assert.assertNotEquals(0, storyList.size());
    }

    @Test
    public void testFindByStoryUPKList() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "putTime");
        Page<Story> storyPage = storyRepository.findByStoryUPKIn(storyEditionRepository.findStoryEditionsByProductId(1), pageRequest);
        Assert.assertNotEquals(0, storyPage.getTotalElements());
    }

}