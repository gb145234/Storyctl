package fscut.manager.demo.service;

import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.vo.MessageVO;

import java.util.List;

public interface MessageService {
    /**
     * 增加需求创建的消息
     * @param story 需求
     * @return 消息
     */
    Message addCreateMessage(Story story);

    /**
     * 增加需求修改的消息
     * @param story 需求
     * @return 消息
     */
    Message addUpdateMessage(Story story);

    /**
     * 读消息
     * @param messageId 消息id
     * @param customerId 用户id
     * @return 读取条数
     */
    Integer readMessage(Integer messageId, Integer customerId);

    /**
     * 发送消息给需求各负责人
     * @param story
     */
    void sendMessage(Story story, Message message);

    /**
     * 读取用户所有未读消息
     * @param customerId 用户id
     * @return 读取条数
     */
    Integer readAll(Integer customerId);

    /**
     * 根据用户id获得未读消息数量
     * @param customerId 用户id
     * @return 未读消息数量
     */
    Integer getUnreadMessageNum(Integer customerId);

    /**
     * 根据用户名获得未读消息数量
     * @param username 用户名
     * @return 未读消息数量
     */
    Integer getUnreadMessageNum(String username);

    /**
     * 删除消息
     * @param messageId 消息id
     * @param customerId 用户id
     * @return 删除条数
     */
    Integer deleteMessage(Integer messageId, Integer customerId);

    /**
     * 根据用户id得到消息列表
     * @param customerId 用户id
     * @return 消息列表
     */
    List<MessageVO> getMessage(Integer customerId);

    /**
     * 删除某用户所有消息
     * @param customerId 用户id
     * @return 删除条数
     */
    Integer deleteAll(Integer customerId);
}
