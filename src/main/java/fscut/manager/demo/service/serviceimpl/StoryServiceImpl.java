package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dao.StoryEditionRepository;
import fscut.manager.demo.dao.StoryRepository;
import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.StoryEdition;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.util.TimeStamp2Date;
import fscut.manager.demo.vo.StoryVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService {

    @Resource
    private StoryRepository storyRepository;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private StoryEditionRepository storyEditionRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Optional<Story> addStory(Story story) {
        StoryUPK storyUPK = getNewStoryUPK(story.getStoryUPK().getProductId());
        BeanUtils.copyProperties(storyUPK, story.getStoryUPK());
        storyRepository.save(story);
        StoryEdition storyEdition = new StoryEdition();
        BeanUtils.copyProperties(story, storyEdition);
        storyEditionRepository.save(storyEdition);
        return storyRepository.findById(storyUPK);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Optional<Story> editStory(Story story) {

        int edition = story.getStoryUPK().getEdition() + 1;
        story.getStoryUPK().setEdition(edition);
        storyRepository.save(story);

        StoryEdition storyEdition = new StoryEdition();
        BeanUtils.copyProperties(story, storyEdition);
        storyEditionRepository.updateEdition(storyEdition);

        return storyRepository.findById(story.getStoryUPK());
    }

    @Override
    public StoryDetailDTO getStoryInfo(StoryUPK storyUPK) {
        List<Story> storyList = getStoryHistory(storyUPK);
        StoryDetailDTO storyDetailDTO = new StoryDetailDTO();
        Optional<Story> story = storyRepository.findById(storyUPK);
        if (story.isPresent()) {
            storyDetailDTO.setStory(story.get());
        }

        for (int i = 0; i < storyList.size() - 1; i++) {
            Story tmp = getDifferenceBetween2Stories(storyList.get(i), storyList.get(i + 1));
            storyDetailDTO.getStoryList().add(tmp);
        }
        return storyDetailDTO;
    }

    private Story getDifferenceBetween2Stories(Story story1, Story story2) {
        Story result = new Story();
        if (!compareString(story1.getConclusion(), story2.getConclusion())) {
            result.setConclusion(story1.getConclusion());
        }
        if (!compareString(story1.getDescription(), story2.getDescription())) {
            result.setDescription(story1.getDescription());
        }
        if (!compareInteger(story1.getDesignId(),story2.getDesignId())) {
            result.setDesignId(story1.getDesignId());
        }
        if (!compareInteger(story1.getDevId(), story2.getDevId())) {
            result.setDevId(story1.getDevId());
        }
        if (!compareInteger(story1.getTestId(),story2.getTestId())) {
            result.setTestId(story1.getTestId());
        }
        if (!compareString(story1.getOrigin(), story2.getOrigin())) {
            result.setOrigin(story1.getOrigin());
        }
        if (!story1.getPutTime().equals(story2.getPutTime())) {
            result.setPutTime(story1.getPutTime());
        }
        if (!compareString(story1.getStoryName(), story2.getStoryName())) {
            result.setStoryName(story1.getStoryName());
        }
        if (!story1.getStoryStatus().equals(story2.getStoryStatus())) {
            result.setStoryStatus(story1.getStoryStatus());
        }
        result.setEditId(story1.getEditId());
        result.setUpdateTime(story1.getUpdateTime());
        return result;
    }

    /**
     * 相同返回true
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true false
     */
    private boolean compareString(String str1, String str2) {
        return str1 == null && str2 == null || str1 != null && str1.equals(str2);
    }

    private boolean compareInteger(Integer integer1, Integer integer2) {
        return integer1 == null && integer2 == null || integer1 != null && integer1.equals(integer2);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteStory(StoryUPK storyUPK) {

        storyRepository.deleteStories(storyUPK);

        storyRepository.deleteEditionByStoryUPK(storyUPK);
    }

    @Override
    public List<StoryUPK> getStoryEditionsByProductId(Integer productId) {
        return storyEditionRepository.findStoryEditionsByProductId(productId);
    }

    /**
     * 获得一个产品的最新版本的需求
     * @param storyUPK 主键
     * @return 需求列表
     */
    @Override
    public List<Story> getStoriesByEditions(List<StoryUPK> storyUPK) {
        List<Story> storyList = new ArrayList<>();
        for (StoryUPK s : storyUPK) {
            storyList.add(storyRepository.findStoryByEdition(s));
        }
        return storyList;
    }

    @Override
    public List<Story> getStoriesByProductId(Integer productId, Integer customerId) {
        if(customerRepository.findProductIdsByCustomerId(customerId).contains(productId)){
            return getStoriesByEditions(getStoryEditionsByProductId(productId));
        }
        else{
            return new ArrayList<>();
        }


    }

    @Override
    public List<Story> getStoryHistory(StoryUPK storyUPK) {
        return storyRepository.findStoriesByStoryUPK(storyUPK);
    }


    @Override
    public StoryUPK getNewStoryUPK(Integer productId) {
        StoryUPK storyUPK = new StoryUPK();
        Integer storyId = storyRepository.findLastedStoryId(productId);
        storyUPK.setProductId(productId);
        if (storyId == null) {
            storyUPK.setStoryId(1);
        } else {
            storyUPK.setStoryId(storyId + 1);
        }
        storyUPK.setEdition(1);
        return storyUPK;
    }

    @Override
    public Story convertStoryVO2Story(StoryVO storyVO) {
        Story story = new Story();
        BeanUtils.copyProperties(storyVO, story);
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        story.setEditId(user.getUserId());
        return story;
    }

    /**
     * 按需求名模糊搜索
     * @param storyName 需求名称
     * @return 需求列表
     */
    @Override
    public List<Story> getStoryByStoryNameLike(String storyName) {
        if (storyName == null) {
            return new ArrayList<>();
        }

        return storyRepository.findByStoryNameContaining(storyName);
    }

    /**
     * 按客户描述模糊搜索
     * @param description 客户描述
     * @return 需求列表
     */
    @Override
    public List<Story> getStoryByDescriptionLike(String description) {
        if (description == null) {
            return new ArrayList<>();
        }

        return storyRepository.findByDescriptionContaining(description);
    }

    /**
     * 根据用户输入进行模糊查询
     * @param input 用户输入
     * @param pageable 分页
     * @return 需求
     */
    @Override
    public Page<Story> searchStory(String input, Pageable pageable) {
        List<Story> storyList = new ArrayList<>();
        List<Story> storyNameContainingList = storyRepository.findByStoryNameContaining(input);
        List<Story> storyDescriptionContainingList = storyRepository.findByDescriptionContaining(input);
        if (!storyNameContainingList.isEmpty()) {
            storyList.addAll(storyNameContainingList);
        }
        if (!storyDescriptionContainingList.isEmpty()) {
            storyList.addAll(storyDescriptionContainingList);
        }
        return new PageImpl<>(storyList, pageable, storyList.size());
    }

    @Override
    public Page<Story> selectStory(Integer productId, Long startTime, Long endTime, String origin, String input, Pageable pageable) {
        List<StoryUPK> storyUPKList = getStoryEditionsByProductId(productId);
        List<Story> storyList = new ArrayList<>();
        Story story;
        for (StoryUPK storyUPK : storyUPKList) {
            story = selectStory(storyUPK, startTime, endTime, origin, input);
            if (story != null) {
                storyList.add(story);
            }
        }
        return new PageImpl<>(storyList, pageable, storyList.size());
    }

    private Story selectStory(StoryUPK storyUPK, Long startTime, Long endTime, String origin, String input) {
        Specification<Story> predicate = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startTime != null) {
                String s = TimeStamp2Date.convert(startTime);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("putTime").as(String.class), s));
            }
            if (endTime != null) {
                String s = TimeStamp2Date.convert(endTime);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("putTime").as(String.class), s));
            }
            if (origin != null) {
                predicates.add(criteriaBuilder.equal(root.get("origin").as(String.class), origin));
            }
            if (input != null) {
                Predicate p1 = criteriaBuilder.like(root.get("storyName").as(String.class), "%" + input + "%");
                Predicate p2 = criteriaBuilder.like(root.get("description").as(String.class), "%" + input + "%");
                predicates.add(criteriaBuilder.or(p1, p2));
            }
            predicates.add(criteriaBuilder.equal(root.get("storyUPK").as(StoryUPK.class), storyUPK));
            Predicate[] pre = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(pre)).getRestriction();
        };
        return storyRepository.findOne(predicate);
    }

}
