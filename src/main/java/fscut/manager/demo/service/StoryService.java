package fscut.manager.demo.service;

import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.vo.StoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface StoryService {

    Optional<Story> addStory(Story story);

    Optional<Story> editStory(Story story);

    Integer deleteStory(StoryUPK storyUPK);

    List<StoryUPK> getStoryEditionsByProductId(Integer productId);

    List<Story> getStoriesByEditions(List<StoryUPK> storyUPKS);

    List<Story> getStoryHistory(StoryUPK storyUPK);

    StoryUPK getNewStoryUPK(Integer productId);

    StoryDetailDTO getStoryInfo(StoryUPK storyUPK);

    Page<Story> getStoriesByProductId(Integer productId, Integer customerId, Pageable pageable);

    List<Story> getStoriesByProductId(Integer productId, Integer customerId);

    Story convertStoryVO2Story(StoryVO storyVO);


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
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @param origin 来源
     * @param input 用户输入
     * @return 需求分页
     */
    Page<Story> selectStory(Integer productId, String startTime, String endTime, String origin, String input, Pageable pageable);
}
