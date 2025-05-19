package com.wxs.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    // 用户基础信息
    private Integer id;
    private String username;

    // 安全相关字段
    private String password;
    private String token;
    
    // 权限控制字段
    private List<String> roles;
    private List<String> permissions;
    
    // 业务扩展字段
    private String avatar;
    private Integer memberLevel;

    public UserDTO() {
    }

    public UserDTO(String token) {
        this.token = token;
    }

    public UserDTO(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
