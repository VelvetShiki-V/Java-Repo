package com.vs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String tel;
    private String prime;
    private Integer uid;

    // 全参构造(除autoincrement uid)
    public User(String username, String password, String tel, String prime) {
        this.username = username;
        this.password = password;
        this.tel = tel;
        this.prime = prime;
    }
}
