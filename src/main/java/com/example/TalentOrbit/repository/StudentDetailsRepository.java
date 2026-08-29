package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long> {
    List<StudentDetails> findByAisheCode(String aisheCode);
    List<StudentDetails> findByBranch(String branch);
    Optional<StudentDetails> findByUser(User user);
}
