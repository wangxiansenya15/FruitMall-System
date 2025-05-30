package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wxs.pojo.dto.Role;
import com.wxs.pojo.dto.UserStatus;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "role")
    @EnumValue
    private Role role;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(exist = false)
    private UserDetail details;

    //  枚举字段,用于表示用户状态，默认为正常
    @TableField(exist = false)
    private UserStatus status;

    @TableField(value = "register_time")
    private Date registerTime;



    @TableField(value = "updateInfo_time")
    private Date updateTime;


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
