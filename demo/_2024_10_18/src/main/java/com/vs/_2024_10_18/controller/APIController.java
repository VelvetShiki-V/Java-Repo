package com.vs._2024_10_18.controller;

import com.vs._2024_10_18.model.Result;
import com.vs._2024_10_18.model.ResultBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth controller", description = "auth api测试专用")
public class APIController {

    @GetMapping
    @Operation(description = "auth get test")
    public Result authGet() {
        return Result.builder().build();
    }

    @PostMapping
    public Result authPost() {
        return Result.builder().build();
    }

    @PutMapping
    public Result authPut() {
        return Result.builder().build();
    }

    @DeleteMapping
    public Result authDel() {
        return Result.builder().build();
    }
}
