package fscut.manager.demo.config;

import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setMessageService(MessageService messageService){
        WebSocketServer.messageService = messageService;
    }

}
