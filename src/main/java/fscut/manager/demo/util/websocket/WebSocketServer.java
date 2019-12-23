package fscut.manager.demo.util.websocket;

import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.token.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{token}", encoders = {SocketEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    private static MessageService messageService;

    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String token;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws Exception{
        this.session = session;
        this.token = token;
        String username = JwtUtils.getUsername(token);
        webSocketMap.put(username, this);
        log.info(username+" has login");
        WebSocketServer.sendInfo(messageService.getUnreadMessageNum(username), username);
    }
    @OnClose
    public void onClose(){
        log.info("exits");
    }

    @OnMessage
    public void onMessage(String message, Session session){

    }

    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }

    public void sendMessage(Object message) throws Exception{
        this.session.getBasicRemote().sendObject(message);

    }
    public static void sendInfo(Object message, String username) throws Exception{
        System.out.println(webSocketMap.get(username));
        webSocketMap.get(username).sendMessage(message);
    }

    public static void sendInfo(Object message) throws Exception{
        webSocketMap.forEach((k,v)->{
            try{
                v.sendMessage(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}
