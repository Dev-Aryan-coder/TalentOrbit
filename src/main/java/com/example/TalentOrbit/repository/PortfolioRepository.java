package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Portfolio;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.PortfolioItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(User user);
    List<Portfolio> findByUserIdAndItemType(Long userId, PortfolioItemType itemType);
}
