package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.TrainingProgram;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    List<TrainingProgram> findByInstitutionUser(User institutionUser);
}
