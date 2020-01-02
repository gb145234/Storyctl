package fscut.manager.demo.enums;

import lombok.Getter;

@Getter
public enum  StoryStatusEnum implements CodeEnum {
    NEW(0, "新需求"),
    PROCESSING(1, "处理中"),
    FINISHED(2, "已完成"),
    CANCEL(3, "已取消")
    ;

    private Integer code;

    private String message;

    StoryStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(Integer code){
        for(StoryStatusEnum s : StoryStatusEnum.values()){
            if(s.getCode() == code){
                return s.getMessage();
            }
        }
        return null;
    }

}
