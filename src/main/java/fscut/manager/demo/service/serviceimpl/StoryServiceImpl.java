package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dao.StoryDetailRepository;
import fscut.manager.demo.dao.StoryEditionRepository;
import fscut.manager.demo.dao.StoryRepository;
import fscut.manager.demo.dto.CustomerListDTO;
import fscut.manager.demo.dto.StoryDetailDTO;
import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.entity.StoryDetail;
import fscut.manager.demo.entity.StoryEdition;
import fscut.manager.demo.entity.UPK.StoryUPK;
import fscut.manager.demo.enums.StoryStatusEnum;
import fscut.manager.demo.service.StoryService;
import fscut.manager.demo.vo.StoryDetailVO;
import fscut.manager.demo.vo.StoryVO;
import org.apache.lucene.search.Query;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private StoryDetailRepository storyDetailRepository;

    private EntityManagerFactory entityManagerFactory;

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
        Story lastStory = storyRepository.findStoryByEdition(story.getStoryUPK());

        int edition = story.getStoryUPK().getEdition() + 1;
        story.getStoryUPK().setEdition(edition);
        story = storyRepository.save(story);

        getDifferenceBetween2Stories(story, lastStory);

        StoryEdition storyEdition = new StoryEdition();
        BeanUtils.copyProperties(story, storyEdition);
        storyEditionRepository.updateEdition(storyEdition);

        return storyRepository.findById(story.getStoryUPK());
    }

    @Override
    public StoryDetailVO getStoryInfo(StoryUPK storyUPK) {
        Integer productId = storyUPK.getProductId();
        Integer storyId = storyUPK.getStoryId();

        StoryUPK newStoryUPK = storyEditionRepository.findStoryEditionsByProductIdAndStoryId(productId,storyId).get(0);

        List<StoryDetail> storyDetails = storyDetailRepository.getStoryDetailsByProductIdAndStoryId(productId, storyId);
        if(storyDetails.size() == 0){
            Optional<Story> story = storyRepository.findById(newStoryUPK);
            StoryDetailVO result = new StoryDetailVO();
            result.setStory(story.get());
            result.setEditable(true);
            return result;
        }

        StoryDetail[] storyDetailsArray = new StoryDetail[storyDetails.size()];
        storyDetails.toArray(storyDetailsArray);
        int oneTimeDetailsNum = 1;
        for(int i = 0; i < storyDetailsArray.length-1; i++){
            if(!storyDetailsArray[i].getEditTime().equals(storyDetailsArray[i+1].getEditTime()) ){
                oneTimeDetailsNum++;
            }
        }
        System.out.println(oneTimeDetailsNum);
        int[] contents = new int[oneTimeDetailsNum];
        for(int i = 0; i < oneTimeDetailsNum; i++){
            contents[i] = 0;
        }
        for(int i = 0, j = 0; i < storyDetailsArray.length -1; i++){
            if(storyDetailsArray[i].getEditTime().equals(storyDetailsArray[i+1].getEditTime()) ){
                contents[j]++;
            }else{
                contents[j]++;
                j++;
            }
        }


        StoryDetailVO result = new StoryDetailVO(productId, storyId, false);
        if(newStoryUPK.getEdition().equals(storyUPK.getEdition())){
            result.setEditable(true);
        }
        Optional<Story> story = storyRepository.findById(newStoryUPK);
        result.setStory(story.get());
        StoryDetailVO.OneTimeDetail[] oneTimeDetailsArray = new StoryDetailVO.OneTimeDetail[oneTimeDetailsNum];
        for(int i = 0, j =0; i < oneTimeDetailsNum; i++){
            oneTimeDetailsArray[i] = result.createOne(storyDetails.get(j).getEditTime(),storyDetails.get(j).getEditName());
            StoryDetailVO.Content[] contentArray = new StoryDetailVO.Content[contents[i]];
            for(int m = 0; m < contents[i]; m++, j++) {
                contentArray[m] = oneTimeDetailsArray[i].createOne(storyDetails.get(j).getAttribute(),storyDetails.get(j).getPrevious(),storyDetails.get(j).getModified());
                oneTimeDetailsArray[i].addOne(contentArray[m]);
            }
            result.addOne(oneTimeDetailsArray[i]);
        }
        return result;

    }


    private void getDifferenceBetween2Stories(Story newStory, Story lastStory) {
        if (!compareString(newStory.getConclusion(), lastStory.getConclusion())) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("讨论结论");
            result.setPrevious(lastStory.getConclusion());
            result.setModified(newStory.getConclusion());
            storyDetailRepository.save(result);
        }
        if (!compareString(newStory.getDescription(), lastStory.getDescription())) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("客户描述");
            result.setPrevious(lastStory.getDescription());
            result.setModified(newStory.getDescription());
            storyDetailRepository.save(result);
        }
        if (!compareInteger(newStory.getDesignId(),lastStory.getDesignId())) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("设计负责人");
            result.setPrevious(customerRepository.findRealNameByCustomerId(lastStory.getDesignId()));
            result.setModified(customerRepository.findRealNameByCustomerId(newStory.getDesignId()));
            storyDetailRepository.save(result);
        }
        if (!compareInteger(newStory.getDevId(), lastStory.getDevId())) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("开发负责人");
            result.setPrevious(customerRepository.findRealNameByCustomerId(lastStory.getDevId()));
            result.setModified(customerRepository.findRealNameByCustomerId(newStory.getDevId()));
            storyDetailRepository.save(result);
        }
        if (Boolean.FALSE.equals(compareString(newStory.getTestId(), lastStory.getTestId()))) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("测试负责人");
            result.setPrevious(customerRepository.findRealNameByCustomerId(lastStory.getTestId()));
            result.setModified(customerRepository.findRealNameByCustomerId(newStory.getTestId()));
            storyDetailRepository.save(result);
        }
        if (Boolean.FALSE.equals(compareString(newStory.getOrigin(), lastStory.getOrigin()))) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("需求来源");
            result.setPrevious(lastStory.getOrigin());
            result.setModified(newStory.getOrigin());
            storyDetailRepository.save(result);
        }
        if (Boolean.FALSE.equals(compareString(newStory.getStoryName(), lastStory.getStoryName()))) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("需求名称");
            result.setPrevious(lastStory.getStoryName());
            result.setModified(newStory.getStoryName());
            storyDetailRepository.save(result);
        }
        if (Boolean.FALSE.equals(compareString(newStory.getStoryStatus(), lastStory.getStoryStatus()))) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("需求状态");
            result.setPrevious(StoryStatusEnum.getMessage(lastStory.getStoryStatus()));
            result.setModified(StoryStatusEnum.getMessage(lastStory.getStoryStatus()));
            storyDetailRepository.save(result);
        }
        if (Boolean.FALSE.equals(compareString(newStory.getTestTime(), lastStory.getTestTime()))) {
            StoryDetail result = new StoryDetail();
            result.setProductId(newStory.getStoryUPK().getProductId());
            result.setStoryId(newStory.getStoryUPK().getStoryId());
            result.setEditTime(newStory.getUpdateTime());
            result.setEditName(customerRepository.findRealNameByCustomerId(newStory.getEditId()));
            result.setAttribute("测试时间");
            result.setPrevious(String.valueOf(lastStory.getTestTime()));
            result.setModified(String.valueOf(newStory.getTestTime()));
            storyDetailRepository.save(result);
        }
    }

    /**
     * 相同返回true
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true false
     */
    private Boolean compareString(Object str1, Object str2) {
        return str1 == null && str2 == null || str1 != null && str1.equals(str2);
    }

    private Boolean compareInteger(Integer integer1, Integer integer2) {
        return integer1 == null && integer2 == null || integer1 != null && integer1.equals(integer2);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Integer deleteStory(StoryUPK storyUPK) {

        storyRepository.deleteStories(storyUPK);

        return storyRepository.deleteEditionByStoryUPK(storyUPK);
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
    public Page<Story> getStoriesByEditions(List<StoryUPK> storyUPK, Pageable pageable) {
        return storyRepository.findByStoryUPKIn(storyUPK, pageable);
    }

    /**
     * 用于前端展示产品需求，使用分页实现
     * @param productId 产品id
     * @param customerId 用户id
     * @param pageable 分页
     * @return 需求分页
     */
    @Override
    public Page<Story> getStoriesByProductId(Integer productId, Integer customerId, Pageable pageable) {
        if(customerRepository.findRoleByCustomerIdAndProductId(customerId, productId) != null){
            return getStoriesByEditions(getStoryEditionsByProductId(productId), pageable);
        }

        return null;
    }

    /**
     * 用于导出需求，不需要分页功能
     * @param productId 产品id
     * @param customerId 用户id
     * @return 需求列表
     */
    @Override
    public List<Story> getStoriesByProductId(Integer productId, Integer customerId) {
        if(customerRepository.findRoleByCustomerIdAndProductId(customerId, productId) != null) {
            return getStoriesByEditions(getStoryEditionsByProductId(productId));
        }
        return new ArrayList<>();
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

    @Override
    public CustomerListDTO getCustomers(Integer productId) {
        List<Customer> designerList = customerRepository.getCustomersByProductIdAndRole(productId, 1);
        List<Customer> developerList = customerRepository.getCustomersByProductIdAndRole(productId, 2);
        List<Customer> testerList = customerRepository.getCustomersByProductIdAndRole(productId, 3);
        CustomerListDTO customerListDTO = new CustomerListDTO();
        customerListDTO.setDesigner(designerList);
        customerListDTO.setDeveloper(developerList);
        customerListDTO.setTester(testerList);
        return customerListDTO;
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

    @Override
    public Page<Story> selectStory(Integer productId, String startTime, String endTime, String origin, String input, Pageable pageable) {
        //EntityManager entityManager = entityManagerFactory.createEntityManager();
        //FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        //entityManager.getTransaction().begin();
        //QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Story.class).get();

        List<StoryUPK> storyUPKList = getStoryEditionsByProductId(productId);
        Specification<Story> predicate = (root, criteriaQuery, criteriaBuilder) -> {
            String putTime = "putTime";
            List<Predicate> predicates = new ArrayList<>();
            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(putTime).as(String.class), startTime));
            }
            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(putTime).as(String.class), endTime));
            }
            if (origin != null) {
                predicates.add(criteriaBuilder.equal(root.get("origin").as(String.class), origin));
            }
            if (input != null) {
                Predicate p1 = criteriaBuilder.like(root.get("storyName").as(String.class), "%" + input + "%");
                Predicate p2 = criteriaBuilder.like(root.get("description").as(String.class), "%" + input + "%");
                predicates.add(criteriaBuilder.or(p1, p2));
                //Query query = queryBuilder.keyword().onFields("storyName", "description").matching(input).createQuery();
                //javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Story.class);
                //predicates.add((Predicate) persistenceQuery);
            }
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("storyUPK"));
            in.value(storyUPKList);
            predicates.add(in);
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(putTime)));
            Predicate[] pre = new Predicate[predicates.size()];

            //entityManager.getTransaction().commit();
            //entityManager.close();
            return criteriaQuery.where(predicates.toArray(pre)).getRestriction();
        };
        return storyRepository.findAll(predicate, pageable);
    }

    @Override
    public List<Story> getStoryByStoryId(Integer productId, Integer storyId) {
        List<Story> storyList = new ArrayList<>();
        List<StoryUPK> storyUPKList = getStoryEditionsByProductId(productId);
        for (StoryUPK storyUPK : storyUPKList) {
            if (storyUPK.getStoryId().equals(storyId)) {
                storyList.add(storyRepository.findStoryByEdition(storyUPK));
            }
        }
        return storyList;
    }

}
