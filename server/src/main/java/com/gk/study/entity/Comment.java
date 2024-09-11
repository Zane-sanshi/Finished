package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("b_comment")
public class Comment implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String content;
    @TableField
    public String commentTime;
    @TableField
    public String likeCount;
    @TableField
    public String userId;
    @TableField(exist = false)
    public String username; // 用户名
    @TableField(exist = false)
    public String nickname; // 昵称
    @TableField(exist = false)
    public String avatar; // 头像
    @TableField
    public String thingId;
    @TableField(exist = false)
    public String title; // 岗位名称
    @TableField(exist = false)
    public String cover; // 封面
    @TableField(exist = false)
    public Long sort; // 类别

}
