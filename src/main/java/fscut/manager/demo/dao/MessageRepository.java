package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Message;
import fscut.manager.demo.vo.MessageVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    /**
     * 将消息保存至customer_message表中
     * @param messageId 消息id
     * @param customerId 用户id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "insert into customer_message (message_id, customer_id) select ?1,?2 from DUAL where not exists (select customer_id from customer_message where message_id = ?1 and customer_id = ?2)", nativeQuery = true)
    void addCustomerMessage(@Param("message_id") Integer messageId, @Param("customer_id") Integer customerId);

    /**
     * 根据用户id查找消息id列表
     * @param customerId 用户id
     * @return 消息id列表
     */
    @Query(value = "select new fscut.manager.demo.vo.MessageVO(m.messageId, m.storyUPK, m.content, m.createdTime, cm.customerId, cm.checked) from CustomerMessage cm ,Message m where cm.messageId = m.messageId and cm.customerId = :customerId order by m.messageId desc")
    List<MessageVO> getMessageByCustomerId(@Param("customerId") Integer customerId);

    /**
     * 根据消息id和用户id设置消息已读
     * @param messageId 消息id
     * @param customerId 用户id
     * @return 读取条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update customer_message set checked = 1 where message_id = ?1 and customer_id = ?2", nativeQuery = true)
    Integer readMessage(Integer messageId, Integer customerId);

    /**
     * 读取某个用户所有未读消息
     * @param customerId 用户id
     * @return 读取所有消息条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update customer_message set checked = 1 where customer_id = ?1", nativeQuery = true)
    Integer readAll(Integer customerId);

    /**
     * 根据用户id查找未读消息条数
     * @param customerId 用户id
     * @return 未读信息条数
     */
    @Query(value = "select count(*) from  customer_message where customer_id = ?1 and checked = 0", nativeQuery = true)
    Integer getUnreadMessageNum(Integer customerId);

    /**
     * 根据用户名查找未读消息条数
     * @param username 用户名
     * @return 未读信息条数
     */
    @Query(value = "select count(*) from  customer_message as cm left join  customer as c on cm.customer_id = c.id where c.username = ?1 and cm.checked = 0", nativeQuery = true)
    Integer getUnreadMessageNum(String username);

    /**
     * 根据消息id和用户id删除消息
     * @param messageId 消息id
     * @param customerId 用户id
     * @return 删除条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_message where message_id = ?1 and customer_id = ?2", nativeQuery = true)
    Integer deleteCustomerMessage(Integer messageId, Integer customerId);

    /**
     * 删除某个用户所有消息
     * @param customerId 用户
     * @return 返回删除条数
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_message where customer_id = ?1", nativeQuery = true)
    Integer deleteAll(Integer customerId);
}
