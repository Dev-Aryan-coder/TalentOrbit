package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.RoadmapStepStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "roadmap_steps")
public class RoadmapStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoadmapStepStatus status = RoadmapStepStatus.LOCKED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_skill_id")
    private Skill linkedSkill;

    public RoadmapStep() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public RoadmapStepStatus getStatus() { return status; }
    public void setStatus(RoadmapStepStatus status) { this.status = status; }

    public Skill getLinkedSkill() { return linkedSkill; }
    public void setLinkedSkill(Skill linkedSkill) { this.linkedSkill = linkedSkill; }
}
