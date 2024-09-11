package com.gk.study.service;

import com.gk.study.entity.Address;
import com.gk.study.entity.Skills;

import java.util.List;

public interface SkillsService {
    List<Skills> getSkillsList();
    void createSkills(Skills skills);
    void deleteSkills(String id);
    void updateSkills(Skills skills);
}
