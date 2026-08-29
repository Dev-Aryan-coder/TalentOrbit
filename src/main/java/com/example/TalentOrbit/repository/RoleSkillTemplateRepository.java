package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.RoleSkillTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleSkillTemplateRepository extends JpaRepository<RoleSkillTemplate, Long> {
    Optional<RoleSkillTemplate> findByRoleNameIgnoreCase(String roleName);
}
