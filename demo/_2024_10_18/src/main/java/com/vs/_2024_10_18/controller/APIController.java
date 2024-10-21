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
    public Result source1() {
        return Result.builder().build();
    }

    @PostMapping
    public Result source2() {
        return Result.builder().build();
    }

    @PutMapping
    public Result source3() {
        return Result.builder().build();
    }

    @DeleteMapping
    public Result source4() {
        return Result.builder().build();
    }

}
