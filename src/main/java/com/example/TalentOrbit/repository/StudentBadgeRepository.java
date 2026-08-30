package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Badge;
import com.example.TalentOrbit.entity.StudentBadge;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentBadgeRepository extends JpaRepository<StudentBadge, Long> {
    List<StudentBadge> findByUser(User user);
    List<StudentBadge> findByUserId(Long userId);
    Optional<StudentBadge> findByUserAndBadge(User user, Badge badge);
}
