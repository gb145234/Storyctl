package fscut.manager.demo.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class IpConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPoint;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPoint = webServerInitializedEvent.getWebServer().getPort();
    }

    public int getPort() {
        return this.serverPoint;
    }
}
