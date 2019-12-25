package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.entity.CustomerMessage;
import fscut.manager.demo.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void testDeleteMessage() {
        CustomerMessage customerMessage = new CustomerMessage();
        customerMessage.setMessageId(43);
        customerMessage.setCustomerId(1);
        messageService.deleteMessage(43, 1);
    }

}