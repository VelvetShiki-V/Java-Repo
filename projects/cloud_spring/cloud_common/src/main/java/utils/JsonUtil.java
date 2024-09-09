package utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

public class JsonUtil {
    public static String staticFilePath = "D:\\Code\\Gitee\\java-repo\\projects\\myemc_gms\\myemc_gms_parent\\myemc_gms_main\\src\\main\\resources\\static\\example.json";
    public static <T> List<T> fileParser(String filepath, Class<T> type) {
        // Hutool读取.json文件内容并转json串
        File file = new File(filepath);
        String jsonStr = FileUtil.readUtf8String(file);
        // json串反序列化为java对象
        JSONArray arr = JSONUtil.parseArray(jsonStr);
        return JSONUtil.toList(arr, type);
    }
}
