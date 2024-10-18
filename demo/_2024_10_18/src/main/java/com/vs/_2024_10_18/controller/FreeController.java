package com.vs._2024_10_18.controller;

import com.vs._2024_10_18.model.Result;
import com.vs._2024_10_18.model.ResultBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/free")
@Tag(name = "free controller", description = "test api测试专用")
public class FreeController {
    @GetMapping
    @Operation(summary = "测试get api", description = "用于测试get请求返回参数")
    public Result getTest() {
        Result.builder().msg("1").build();
//        throw new CustomException("400", "登陆失败", HttpStatus.UNAUTHORIZED);
        return ResultBuilder
                .aResult()
                .code(Result.SUCCESS_CODE)
                .msg("get获取成功")
                .build();
    }

    @PostMapping
    public Result freePost() {
        return Result.builder().build();
    }

    @PutMapping
    public Result freePut() {
        return Result.builder().build();
    }

    @DeleteMapping
    public Result freeDel() {
        return Result.builder().build();
    }
}
