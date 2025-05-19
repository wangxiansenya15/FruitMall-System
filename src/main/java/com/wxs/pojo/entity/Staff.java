package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("staff")
public class Staff {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(value = "staff_name")
    private String name;

    @EnumValue
    private Role role;

    @TableField(value = "phone_num")
    private String phone;

    private String email;

    private String password;

    @EnumValue
    private StaffStatus status = StaffStatus.ACTIVE;

    public enum StaffStatus {
        ACTIVE, DISABLE
    }
}
