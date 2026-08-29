package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.InstitutionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionDetailsRepository extends JpaRepository<InstitutionDetails, Long> {
    Optional<InstitutionDetails> findByAisheCode(String aisheCode);
}
