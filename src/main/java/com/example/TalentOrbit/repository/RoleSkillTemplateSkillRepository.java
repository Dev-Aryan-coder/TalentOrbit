package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.RoleSkillTemplate;
import com.example.TalentOrbit.entity.RoleSkillTemplateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleSkillTemplateSkillRepository extends JpaRepository<RoleSkillTemplateSkill, Long> {
    List<RoleSkillTemplateSkill> findByTemplate(RoleSkillTemplate template);
}
