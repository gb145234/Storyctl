package fscut.manager.demo.service;

import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;

import java.util.List;

public interface MessageService {
    void addMessage(Story story, String action);
    void readMessage(Integer messageId, Integer customerId);
    Integer getUnreadMessageNum(Integer customerId);
    Integer getUnreadMessageNum(String username);
    void deleteMessage(Integer messageId, Integer customerId);
    List<Message> getMessage(Integer customerId);
    List<Message> getMessage(String username);
}
