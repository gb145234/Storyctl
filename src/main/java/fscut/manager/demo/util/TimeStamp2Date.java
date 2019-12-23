package fscut.manager.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp2Date {
    public static String convert(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long date = new Long(time);
        return format.format(date);
    }
}
