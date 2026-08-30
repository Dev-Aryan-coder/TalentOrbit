package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Skill;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByUser(User user);
    List<StudentSkill> findByUserId(Long userId);
    Optional<StudentSkill> findByUserAndSkill(User user, Skill skill);
    Optional<StudentSkill> findByUserIdAndSkillId(Long userId, Long skillId);
}
