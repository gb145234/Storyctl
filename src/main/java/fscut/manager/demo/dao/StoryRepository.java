package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoryRepository extends JpaRepository<Story, StoryUPK> {

    /**
     * 查找最近需求id
     * @param productId 产品id
     * @return 需求id
     */
    @Query(value = "select story_id from story_edition where product_id = ?1 order by story_id DESC limit 1", nativeQuery = true)
    Integer findLastedStoryId(@Param("productId") Integer productId);

    /**
     * 根据产品id和需求id删除版本
     * @param storyUPK
     * @return
     */
    @Modifying
    @Query(value = "delete from story_edition where product_id = :#{#storyUPK.productId} and story_id = :#{#storyUPK.storyId}", nativeQuery = true)
    Integer deleteEditionByStoryUPK(@Param("storyUPK") StoryUPK storyUPK);

    /**
     * 根据产品id和需求id查找历史版本
     * @param storyUPK 主键
     * @return 版本列表
     */
    @Query(value = "select * from story where product_id = :#{#storyUPK.productId} and story_id = :#{#storyUPK.storyId} order by edition desc ", nativeQuery = true)
    List<Story> findStoriesByStoryUPK(@Param("storyUPK") StoryUPK storyUPK);

    @Modifying
    @Query(value = "delete from story where product_id = :#{#storyUPK.productId} and story_id = :#{#storyUPK.storyId}", nativeQuery = true)
    void deleteStories(@Param("storyUPK") StoryUPK storyUPK);

    @Query(value = "select * from story where product_id = :#{#storyUPK.productId} and " +
            "story_id = :#{#storyUPK.storyId} and edition = :#{#storyUPK.edition}", nativeQuery = true)
    Story findStoryByEdition(@Param("storyUPK") StoryUPK storyUPK);


    /**
     * 根据需求名称模糊查询
     * @param storyName 需求名称
     * @return 需求列表
     */
    List<Story> findByStoryNameContaining(String storyName);

    /**
     * 根据客户描述模糊查询
     * @param description 客户描述
     * @return 需求列表
     */
    List<Story> findByDescriptionContaining(String description);

    /**
     * 根据需求名称和客户描述模糊查询
     * @param storyName 需求名称
     * @param description 客户描述
     * @param pageable 分页
     * @return 分页显示需求
     */
    Page<Story> findByStoryNameContainingAndDescriptionContaining(String storyName, String description, Pageable pageable);

    /**
     * @param specification 动态查询
     * @param pageable 分页
     * @return 分页显示需求
     */
    Page<Story> findAll(Specification<Story> specification, Pageable pageable);

    Story findOne(Specification<Story> specification);
}
