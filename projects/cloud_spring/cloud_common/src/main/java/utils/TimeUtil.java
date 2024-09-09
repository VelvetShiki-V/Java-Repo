package utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    // 获取当日Ts(精确到天)
    public static String getCurrentDay () {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
    }

    // 获取实时时间戳(精确到秒)
    public static long getCurrentTs () {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(2024, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    }
}
