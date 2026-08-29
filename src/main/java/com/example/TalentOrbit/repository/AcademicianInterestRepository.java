package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.AcademicianInterest;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicianInterestRepository extends JpaRepository<AcademicianInterest, Long> {
    List<AcademicianInterest> findByUser(User user);
    List<AcademicianInterest> findByPostingId(Long postingId);
}
