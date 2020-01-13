package fscut.manager.demo.dao;

import fscut.manager.demo.entity.StoryEdition;
import fscut.manager.demo.entity.UPK.StoryUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StoryEditionRepository extends JpaRepository<StoryEdition, StoryUPK> {

    /**
     * 根据产品id查找最新需求主键列表
     * @param productId 产品id
     * @return 主键列表
     */
    @Query("select new fscut.manager.demo.entity.UPK.StoryUPK(s.storyUPK.productId, s.storyUPK.storyId, s.storyUPK.edition) from StoryEdition s where s.storyUPK.productId = :productId")
    List<StoryUPK> findStoryEditionsByProductId(@Param("productId") Integer productId);

    /**
     * 更新最新版本
     * @param storyEdition 需求版本
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update story_edition set edition = :#{#storyEdition.storyUPK.edition} where product_id = :#{#storyEdition.storyUPK.productId} and story_id = :#{#storyEdition.storyUPK.storyId}",nativeQuery = true)
    void updateEdition(@Param("storyEdition") StoryEdition storyEdition);

    /**
     * 根据产品id和需求id查找主键列表
     * @param productId 产品id
     * @param storyId 需求id
     * @return
     */
    @Query("select new fscut.manager.demo.entity.UPK.StoryUPK(s.storyUPK.productId, s.storyUPK.storyId, s.storyUPK.edition) from StoryEdition s where s.storyUPK.productId = :productId and s.storyUPK.storyId = :storyId order by s.storyUPK.edition desc")
    List<StoryUPK> findStoryEditionsByProductIdAndStoryId(@Param("productId") Integer productId, @Param("storyId") Integer storyId);
}
