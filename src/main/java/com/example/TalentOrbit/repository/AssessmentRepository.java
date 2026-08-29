package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Assessment;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByUser(User user);
    List<Assessment> findByUserId(Long userId);
}
