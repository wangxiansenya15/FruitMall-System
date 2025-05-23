package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "email")
    private String email;

    @TableField(value = "role")
    @EnumValue
    private Role role;

    @TableField(exist = false)
    @JsonIgnore // 避免 JSON 序列化时报错
    private UserDetail details;

    //  枚举字段,用于表示用户状态，默认为正常
    @TableField(exist = false)
    @EnumValue
    private UserStatus status;


    /** 数据库存储的四个原始字段
     * 分别表示用户的账户是否禁用、过期、锁定、凭证过期
     */
    @TableField(value = "enabled")
    private boolean enabled;

    @TableField(value = "account_non_expired")
    private boolean accountNonExpired;

    @TableField(value = "account_non_locked")
    private boolean accountNonLocked;

    @TableField(value = "credentials_non_expired")
    private boolean credentialsNonExpired;

    // 枚举视图
    @Transient
    public UserStatus getStatus() {
        return UserStatus.fromBooleans(
                enabled,
                accountNonExpired,
                accountNonLocked,
                credentialsNonExpired
        );
    }

    public void setStatus(UserStatus status) {
        this.enabled = status.isEnabled();
        this.accountNonExpired = status.isAccountNonExpired();
        this.accountNonLocked = status.isAccountNonLocked();
        this.credentialsNonExpired = status.isCredentialsNonExpired();
    }
}
