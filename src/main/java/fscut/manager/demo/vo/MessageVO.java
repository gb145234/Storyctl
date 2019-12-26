package fscut.manager.demo.vo;

import fscut.manager.demo.entity.UPK.StoryUPK;
import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {

    /**
     * 消息id
     */
    private Integer messageId;

    /**
     * 需求主键
     */
    private StoryUPK storyUPK;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 是否已读
     */
    private boolean checked;

    public MessageVO(Integer messageId, StoryUPK storyUPK, String content, Date createdTime, Integer customerId, boolean checked) {
        this.messageId = messageId;
        this.storyUPK = storyUPK;
        this.content = content;
        this.createdTime = createdTime;
        this.customerId = customerId;
        this.checked = checked;
    }
}
