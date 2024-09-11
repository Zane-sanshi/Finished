package com.gk.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.study.entity.Skills;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkillsMapper extends BaseMapper<Skills> {
    List<Skills> getList();
}
