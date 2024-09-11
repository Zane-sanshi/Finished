package com.gk.study.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("b_skills")
public class Skills implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public long userId;
    @TableField
    public String technicalSkills;
    @TableField
    public String certifications;
    @TableField
    public String communication;
    @TableField
    public String teamwork;
    @TableField
    public String problemSolving;
    @TableField
    public String leadership;
    @TableField
    public String continuousLearning;
    @TableField
    public String adaptability;
    @TableField
    public String relevantExperience;
    @TableField
    public String achievements;
    @TableField
    public String responsibility;
    @TableField
    public String stressManagement;
    @TableField
    public String integrity;
    @TableField
    public String industryInsights;
    @TableField
    public String regulatoryKnowledge;
    @TableField
    public String username;
}

