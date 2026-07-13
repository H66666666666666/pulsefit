package com.itbin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthSession {
    private Integer id;
    private String username;
    private String name;
    private String sysRole;
    private String token;
}
