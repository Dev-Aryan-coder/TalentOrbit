package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Report;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByGeneratedBy(User user);
}
