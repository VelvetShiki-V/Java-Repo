package com.vs._2024_10_18.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// entity用于数据库数据映射
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String uid;
    private String role;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean status;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
}
