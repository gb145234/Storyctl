package fscut.manager.demo.dao;

import fscut.manager.demo.entity.StoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoryDetailRepository extends JpaRepository<StoryDetail, Integer> {

    @Query(value = "select * from story_detail where product_id = ?1 and story_id = ?2 order by edit_time DESC ", nativeQuery = true)
    List<StoryDetail> getStoryDetailsByProductIdAndStoryId(Integer productId, Integer storyId);

}
