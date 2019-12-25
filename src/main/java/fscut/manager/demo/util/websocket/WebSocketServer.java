package fscut.manager.demo.util.websocket;

import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.token.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{token}", encoders = {SocketEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    public static MessageService messageService;

    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String token;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        this.token = token;
        String username = JwtUtils.getUsername(token);
        if (username != null) {
            webSocketMap.put(username, this);
        }
        log.info(username + " has login,有新的连接，总数：{}", webSocketMap.size());
        WebSocketServer.sendInfo(messageService.getUnreadMessageNum(username), username);
    }

    @OnClose
    public void onClose() {
        String username = JwtUtils.getUsername(token);
        if (username != null) {
            webSocketMap.remove(username);
        }
        log.info("连接断开，总数：{}", webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端发来的消息：{}", message);
    }

    @OnError
    public void onError(Throwable error) {
        log.info("错误信息为：{}", error.getMessage());
    }

    private void sendMessage(Object message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.info(e.getMessage());
        }
    }

    public static void sendInfo(Object message, String username) {
        log.info("向{}发送了消息：{}", username, message);
        webSocketMap.get(username).sendMessage(message);
    }

    //public static void sendInfo(Object message) {
    //    webSocketMap.forEach((k,v)-> v.sendMessage(message));
    //}

}
