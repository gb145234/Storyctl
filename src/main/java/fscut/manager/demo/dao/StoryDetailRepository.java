package fscut.manager.demo.dao;

import fscut.manager.demo.entity.StoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoryDetailRepository extends JpaRepository<StoryDetail, Integer> {

    /**
     * 根据产品id和需求id查找需求详情列表
     * @param productId 产品id
     * @param storyId 需求id
     * @return 需求详情列表
     */
    @Query(value = "select * from story_detail where product_id = ?1 and story_id = ?2 order by edit_time DESC ", nativeQuery = true)
    List<StoryDetail> getStoryDetailsByProductIdAndStoryId(Integer productId, Integer storyId);

}
