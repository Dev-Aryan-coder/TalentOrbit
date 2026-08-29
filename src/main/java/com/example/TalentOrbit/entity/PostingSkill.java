package com.example.TalentOrbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "posting_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"posting_id", "skill_id"})
})
public class PostingSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", nullable = false)
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = true;

    private Integer weight = 1;

    public PostingSkill() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Posting getPosting() { return posting; }
    public void setPosting(Posting posting) { this.posting = posting; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
