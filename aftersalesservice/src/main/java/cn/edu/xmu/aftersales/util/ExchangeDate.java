package cn.edu.xmu.aftersales.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ExchangeDate {
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd";
    public static Map<Boolean,LocalDateTime> StringToDateTime(String T){

        LocalDateTime localDateTime =null;
        //用于指定 日期/时间 模式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
        boolean flag = true;
        try {
            localDateTime = LocalDateTime.parse(T, dtf);
        } catch (Exception e) {
            flag = false;
        }
        Map<Boolean,LocalDateTime> m = new HashMap<>();
        m.put(flag, localDateTime);
        return m;
    }

    public static Map<Boolean, LocalDateTime> DateTimeStringToLocalDataTime(String T){

        LocalDateTime localDateTime =null;
        //用于指定 日期/时间 模式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
        boolean flag = true;
        try {
            localDateTime = LocalDate.parse(T, dtf).atStartOfDay();
        } catch (Exception e) {
            flag = false;
        }
        Map<Boolean,LocalDateTime> m = new HashMap<>();
        m.put(flag, localDateTime);
        return m;

    }
}
