package fscut.manager.demo.service;

import fscut.manager.demo.dto.CustomerListDTO;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.vo.StoryDetailVO;
import fscut.manager.demo.vo.StoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface StoryService {

    /**
     * 增加需求
     * @param story 需求
     * @return 新需求
     */
    Optional<Story> addStory(Story story);

    /**
     * 编辑需求
     * @param story 需求
     * @return 新需求
     */
    Optional<Story> editStory(Story story);

    /**
     * 删除需求
     * @param storyUPK 主键
     * @return 删除条数
     */
    Integer deleteStory(StoryUPK storyUPK);

    /**
     * 根据产品id查找主键列表
     * @param productId 产品id
     * @return 主键列表
     */
    List<StoryUPK> getStoryEditionsByProductId(Integer productId);

    /**
     * 根据主键列表查找需求列表
     * @param storyUPKS 主键列表
     * @return 需求列表
     */
    List<Story> getStoriesByEditions(List<StoryUPK> storyUPKS);

    /**
     * 根据主键列表得到分页需求
     * @param storyUPKS 主键列表
     * @param pageable 分页
     * @return 分页需求
     */
    Page<Story> getStoriesByEditions(List<StoryUPK> storyUPKS, Pageable pageable);

    /**
     * 根据主键得到需求列表
     * @param storyUPK 主键
     * @return 需求列表
     */
    List<Story> getStoryHistory(StoryUPK storyUPK);

    /**
     * 根据产品id得到主键
     * @param productId 产品Id
     * @return 主键
     */
    StoryUPK getNewStoryUPK(Integer productId);

    /**
     * 根据主键得到需求详情
     * @param storyUPK 主键
     * @return 需求详情
     */
    StoryDetailVO getStoryInfo(StoryUPK storyUPK);

    /**
     * 根据产品id和客户id得到需求分页
     * @param productId 产品id
     * @param customerId 客户id
     * @param pageable 分页
     * @return 分页需求
     */
    Page<Story> getStoriesByProductId(Integer productId, Integer customerId, Pageable pageable);

    /**
     * 根据产品id和客户id得到需求列表
     * @param productId 产品id
     * @param customerId 用户id
     * @return 需求列表
     */
    List<Story> getStoriesByProductId(Integer productId, Integer customerId);

    /**
     * 将storyVO转换为story
     * @param storyVO 视图story
     * @return 需求
     */
    Story convertStoryVO2Story(StoryVO storyVO);

    /**
     * 根据产品id得到客户列表
     * @param productId 产品Id
     * @return 客户列表
     */
    CustomerListDTO getCustomers(Integer productId);

    /**
     * 需求名称模糊查询
     * @param storyName 需求名称
     * @return 需求列表
     */
    List<Story> getStoryByStoryNameLike(String storyName);

    /**
     * 客户描述模糊查询
     * @param description 客户描述
     * @return 需求列表
     */
    List<Story> getStoryByDescriptionLike(String description);

    /**
     * 筛选即搜索需求
     * @param productId 产品id
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @param origin 来源
     * @param input 用户输入
     * @param pageable 分页
     * @return 需求分页
     */
    Page<Story> selectStory(Integer productId, String startTime, String endTime, String origin, String input, Pageable pageable);

    /**
     * 根据产品id和需求id查找需求
     * @param productId 产品id
     * @param storyId 需求id
     * @return 需求
     */
    List<Story> getStoryByStoryId(Integer productId, Integer storyId);
}
