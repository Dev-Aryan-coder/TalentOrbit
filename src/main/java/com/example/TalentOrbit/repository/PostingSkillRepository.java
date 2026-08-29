package com.example.TalentOrbit.repository;

import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.PostingSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingSkillRepository extends JpaRepository<PostingSkill, Long> {
    List<PostingSkill> findByPosting(Posting posting);
    List<PostingSkill> findByPostingId(Long postingId);
}
