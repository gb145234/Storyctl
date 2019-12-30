package fscut.manager.demo.dao;

import fscut.manager.demo.entity.StoryEdition;
import fscut.manager.demo.entity.UPK.StoryUPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StoryEditionRepository extends JpaRepository<StoryEdition, StoryUPK> {

    @Query("select new fscut.manager.demo.entity.UPK.StoryUPK(s.storyUPK.productId, s.storyUPK.storyId, s.storyUPK.edition) from StoryEdition s where s.storyUPK.productId = :productId") // order by s.storyUPK.storyId desc
    List<StoryUPK> findStoryEditionsByProductId(@Param("productId") Integer productId);

    @Modifying
    @Query(value = "update story_edition set edition = :#{#storyEdition.storyUPK.edition} where product_id = :#{#storyEdition.storyUPK.productId} and story_id = :#{#storyEdition.storyUPK.storyId}",nativeQuery = true)
    void updateEdition(@Param("storyEdition") StoryEdition storyEdition);
}
