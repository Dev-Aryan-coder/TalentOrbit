package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.RoadmapStep;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapStepRepository extends JpaRepository<RoadmapStep, Long> {
    List<RoadmapStep> findByUserOrderByStepOrderAsc(User user);
    List<RoadmapStep> findByUserIdOrderByStepOrderAsc(Long userId);
}
