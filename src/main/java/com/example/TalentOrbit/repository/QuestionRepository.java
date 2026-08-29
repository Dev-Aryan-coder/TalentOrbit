package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySkillId(Long skillId);
    List<Question> findBySkillIdAndTopic(Long skillId, String topic);
}
