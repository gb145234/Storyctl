package fscut.manager.demo.dao;

import fscut.manager.demo.entity.UPK.StoryUPK;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryEditionRepositoryTest {

    @Resource
    private StoryEditionRepository storyEditionRepository;

    @Test
    public void findStoryEditionsByProductId() {
        List<StoryUPK> storyUPKList = storyEditionRepository.findStoryEditionsByProductId(1);
        for (StoryUPK storyUPK : storyUPKList) {
            System.out.println(storyUPK.toString());
        }
    }

}