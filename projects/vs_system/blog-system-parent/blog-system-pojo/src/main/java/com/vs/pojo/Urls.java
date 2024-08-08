package com.vs.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")      // 通过静态方法全参构造
public class Urls {
    // urls为键名，存储的值为字符串数组
    private List<String> urls;
}
