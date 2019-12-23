package fscut.manager.demo.util.websocket;

import com.alibaba.fastjson.JSON;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SocketEncoder implements Encoder.Text<Object>{
    @Override
    public String encode(Object obj) throws EncodeException {
        return JSON.toJSONString(obj);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
