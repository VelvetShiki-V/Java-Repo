package com.vs.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatUtil {
    // 日期格式化
    public static LocalDateTime timeFormat(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowString = now.format(formatter);
        return LocalDateTime.parse(nowString, formatter);
    }
}
