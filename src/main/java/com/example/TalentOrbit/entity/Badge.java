package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.BadgeCriteriaType;
import jakarta.persistence.*;

@Entity
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "criteria_type", nullable = false)
    private BadgeCriteriaType criteriaType;

    @Column(name = "criteria_value")
    private Integer criteriaValue;

    public Badge() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public BadgeCriteriaType getCriteriaType() { return criteriaType; }
    public void setCriteriaType(BadgeCriteriaType criteriaType) { this.criteriaType = criteriaType; }

    public Integer getCriteriaValue() { return criteriaValue; }
    public void setCriteriaValue(Integer criteriaValue) { this.criteriaValue = criteriaValue; }
}
