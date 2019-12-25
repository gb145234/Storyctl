package fscut.manager.demo.service;

import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;

import java.util.List;

public interface MessageService {
    /**
     * 增加需求创建的消息
     * @param story 需求
     */
    void addCreateMessage(Story story);

    /**
     * 增加需求修改的消息
     * @param story 需求
     */
    void addUpdateMessage(Story story);

    /**
     * 读消息
     * @param messageId 消息id
     * @param customerId 用户id
     */
    void readMessage(Integer messageId, Integer customerId);

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
     */
    Integer deleteMessage(Integer messageId, Integer customerId);

    /**
     * 根据用户id得到消息列表
     * @param customerId 用户id
     * @return 消息列表
     */
    List<Message> getMessage(Integer customerId);

    /**
     * 根据用户名得到消息列表
     * @param username 用户名
     * @return 消息列表
     */
    List<Message> getMessage(String username);
}
