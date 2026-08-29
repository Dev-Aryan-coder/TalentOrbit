package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Application;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByPosting(Posting posting);
    List<Application> findByPostingId(Long postingId);
    List<Application> findByStatus(ApplicationStatus status);
    Optional<Application> findByPostingIdAndUserId(Long postingId, Long userId);
}
