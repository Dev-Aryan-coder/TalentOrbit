package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.FlagItemType;
import com.example.TalentOrbit.enums.FlagStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flags")
public class Flag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private FlagItemType itemType;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlagStatus status = FlagStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Flag() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getReportedBy() { return reportedBy; }
    public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }

    public FlagItemType getItemType() { return itemType; }
    public void setItemType(FlagItemType itemType) { this.itemType = itemType; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public FlagStatus getStatus() { return status; }
    public void setStatus(FlagStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
