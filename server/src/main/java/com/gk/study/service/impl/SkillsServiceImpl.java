package com.gk.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.entity.Skills;
import com.gk.study.mapper.SkillsMapper;
import com.gk.study.service.SkillsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsServiceImpl extends ServiceImpl<SkillsMapper, Skills> implements SkillsService {
    final
    SkillsMapper mapper;

    public SkillsServiceImpl(SkillsMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Skills> getSkillsList() {
        return mapper.getList();
    }

    @Override
    public void createSkills(Skills skills) {
        mapper.insert(skills);
    }

    @Override
    public void deleteSkills(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateSkills(Skills skills) {
        mapper.updateById(skills);
    }
}

