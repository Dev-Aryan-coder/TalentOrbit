package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Flag;
import com.example.TalentOrbit.enums.FlagStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlagRepository extends JpaRepository<Flag, Long> {
    List<Flag> findByStatus(FlagStatus status);
}
