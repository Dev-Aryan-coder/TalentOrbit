package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Question;
import com.example.TalentOrbit.enums.TechType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySkillId(Long skillId);
    List<Question> findBySkillName(String skillName);
    List<Question> findByTechType(TechType techType);
    List<Question> findByLanguageIgnoreCase(String language);
    List<Question> findByFrameworkIgnoreCase(String framework);
    List<Question> findByTechNameIgnoreCase(String techName);
}
