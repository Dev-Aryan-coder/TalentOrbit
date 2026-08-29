package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.AcademicianInterestTag;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicianInterestTagRepository extends JpaRepository<AcademicianInterestTag, Long> {
    List<AcademicianInterestTag> findByUser(User user);
}
