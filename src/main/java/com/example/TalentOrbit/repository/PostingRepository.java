package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.PostingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findByPostedBy(User user);
    List<Posting> findByPostingTypeAndIsActiveTrue(PostingType postingType);
    List<Posting> findByIsActiveTrue();
}
