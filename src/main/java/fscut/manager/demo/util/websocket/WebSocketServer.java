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
        log.info(username + " has login");
        WebSocketServer.sendInfo(messageService.getUnreadMessageNum(username), username);
    }

    @OnClose
    public void onClose() {
        log.info("exits");
    }

    @OnMessage
    public void onMessage(String message) {
        // do nothing because of sendMessage
    }

    @OnError
    public void onError(Throwable error) {
        log.info(error.getMessage());
    }

    private void sendMessage(Object message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.info(e.getMessage());
        }
    }

    public static void sendInfo(Object message, String username) {
        log.info(String.valueOf(webSocketMap.get(username)));
        webSocketMap.get(username).sendMessage(message);
    }

    public static void sendInfo(Object message) {
        webSocketMap.forEach((k,v)-> v.sendMessage(message));
    }

}
