package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.CollaborationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "academician_interests", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "posting_id"})
})
public class AcademicianInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", nullable = false)
    private Posting posting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaborationStatus status = CollaborationStatus.INTERESTED;

    @Column(name = "expressed_at", nullable = false)
    private LocalDateTime expressedAt = LocalDateTime.now();

    public AcademicianInterest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Posting getPosting() { return posting; }
    public void setPosting(Posting posting) { this.posting = posting; }

    public CollaborationStatus getStatus() { return status; }
    public void setStatus(CollaborationStatus status) { this.status = status; }

    public LocalDateTime getExpressedAt() { return expressedAt; }
    public void setExpressedAt(LocalDateTime expressedAt) { this.expressedAt = expressedAt; }
}
