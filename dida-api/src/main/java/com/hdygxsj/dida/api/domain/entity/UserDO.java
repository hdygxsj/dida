package com.hdygxsj.dida.api.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dida_user")
public class UserDO {
    private String username;
    private String password;
    @TableField("super")
    private boolean superUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private byte type;
}
