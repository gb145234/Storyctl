package fscut.manager.demo.controller;


import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.CustomerMessage;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController{

     @Resource
     private MessageService messageService;

     @GetMapping("getNum")
     public ResponseEntity<Integer> getUnreadMessageNum(){
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          return ResponseEntity.ok(messageService.getUnreadMessageNum(user.getUserId()));
     }

     @GetMapping("getMessageList")
     public ResponseEntity<List<Message>> getMessageList(){
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          List<Message> messageList = messageService.getMessage(user.getUserId());
          return ResponseEntity.ok(messageList);
     }

     @PostMapping("readMessage")
     public ResponseEntity<Message> readMessage(@RequestBody CustomerMessage cMessage) {
          messageService.readMessage(cMessage.getMessageId(), cMessage.getCustomerId());
          return ResponseEntity.ok(null);
     }

     @DeleteMapping("deleteMessage")
     public ResponseEntity<Message> deleteMessage(@RequestBody CustomerMessage cMessage){
          messageService.deleteMessage(cMessage.getMessageId(), cMessage.getCustomerId());
          return ResponseEntity.ok(null);
     }

     @GetMapping("/socket/push")
     public Object pushToWeb() {
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          Integer num = messageService.getUnreadMessageNum(user.getUserId());
          if(num != 0) {
               WebSocketServer.sendInfo(messageService.getMessage(user.getUserId()), user.getUsername());
          }
          return "good";
     }

}
