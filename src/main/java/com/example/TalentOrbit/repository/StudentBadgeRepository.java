package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.StudentBadge;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentBadgeRepository extends JpaRepository<StudentBadge, Long> {
    List<StudentBadge> findByUser(User user);
    List<StudentBadge> findByUserId(Long userId);
}
