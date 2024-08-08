package com.vs.myemc_gms_main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.vs.pojo.Model;
import com.vs.utils.JsonUtil;
import com.vs.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class MyemcGmsMainApplicationTests {
    // jackson解析json
//    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void readJson() throws IOException {
        // Hutool读取
        File file = new File("src/main/resources/static/example.json");
        String jsonStr = FileUtil.readUtf8String(file);
        // java对象转换
        JSONArray arr = JSONUtil.parseArray(jsonStr);
        List<Model> list = JSONUtil.toList(arr, Model.class);
        for(Model m : list) {
            System.out.println(m);
        }
    }

    @Test
    void fileReader() {
        List<Model> models = JsonUtil.fileParser("src/main/resources/static/example.json", Model.class);
        for(Model m : models) System.out.println(m);
    }

}
