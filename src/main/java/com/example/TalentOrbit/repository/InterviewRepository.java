package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Application;
import com.example.TalentOrbit.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplication(Application application);
    List<Interview> findByApplicationId(Long applicationId);
}
