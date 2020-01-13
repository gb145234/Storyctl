package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dao.MessageRepository;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import fscut.manager.demo.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private MessageRepository messageRepository;

    @Resource
    private WebSocketServer webSocketServer;

    @Resource
    private CustomerService customerService;

    @Override
    public Message addCreateMessage(Story story) {
        if(story == null) {
            return new Message();
        }
        String content = String.format("%s %s %s 需求", customerRepository.findRealNameByCustomerId(story.getEditId()), "新建了", story.getStoryName());
        Message message = new Message();
        BeanUtils.copyProperties(story, message);
        message.setContent(content);
        message = messageRepository.save(message);
        messageRepository.addCustomerMessage(message.getMessageId(), story.getEditId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDesignId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDevId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getTestId());
        return message;
    }

    @Override
    public Message addUpdateMessage(Story story) {
        if (story == null) {
            return new Message();
        }
        Message message = new Message();
        BeanUtils.copyProperties(story, message);
        String content = String.format("%s %s %s 需求", customerRepository.findRealNameByCustomerId(story.getEditId()), "修改了", story.getStoryName());
        message.setContent(content);
        message = messageRepository.save(message);
        messageRepository.addCustomerMessage(message.getMessageId(), story.getEditId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDesignId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDevId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getTestId());
        return message;
    }

    @Override
    public void sendMessage(Story story, Message message) {
        Integer designId = story.getDesignId();
        Integer devId = story.getDevId();
        Integer testId = story.getTestId();
        if (designId != null && WebSocketServer.webSocketMap.get(customerService.getUsernameById(designId)) != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(designId));
        }
        if (devId != null && WebSocketServer.webSocketMap.get(customerService.getUsernameById(devId)) != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(devId));
        }
        if (testId != null && WebSocketServer.webSocketMap.get(customerService.getUsernameById(testId)) != null) {
            webSocketServer.sendInfo(message.getContent(), customerService.getUsernameById(testId));
        }
    }

    @Override
    public List<MessageVO> getMessage(Integer customerId) {
        return messageRepository.getMessageByCustomerId(customerId);
    }

    @Override
    public Integer deleteAll(Integer customerId) {
        return messageRepository.deleteAll(customerId);
    }

    @Override
    public Integer readMessage(Integer messageId, Integer customerId) {
        return messageRepository.readMessage(messageId, customerId);
    }

    @Override
    public Integer readAll(Integer customerId) {
        return messageRepository.readAll(customerId);
    }

    @Override
    public Integer getUnreadMessageNum(Integer customerId) {
        return messageRepository.getUnreadMessageNum(customerId);
    }

    @Override
    public Integer getUnreadMessageNum(String username) {
        return messageRepository.getUnreadMessageNum(username);
    }

    @Override
    public Integer deleteMessage(Integer messageId, Integer customerId) {
        return messageRepository.deleteCustomerMessage(messageId, customerId);
    }

}
