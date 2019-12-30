package fscut.manager.demo.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRepositoryTest {

    @Resource
    public MessageRepository messageRepository;

    @Test
    public void readAll() {
        messageRepository.readAll(5);
    }

    @Test
    public void deleteAll() {
        Integer res = messageRepository.deleteAll(2);
        Assert.assertNotEquals(java.util.Optional.of(0), res);
    }

}