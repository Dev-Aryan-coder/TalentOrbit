package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.AcademicianDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicianDetailsRepository extends JpaRepository<AcademicianDetails, Long> {
    List<AcademicianDetails> findByAisheCode(String aisheCode);
}
