package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.TechType;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private String topic; // e.g. "JOIN", "Virtual Threads", "Hooks", "Docker Compose"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "option_a", nullable = false)
    private String optionA;

    @Column(name = "option_b", nullable = false)
    private String optionB;

    @Column(name = "option_c", nullable = false)
    private String optionC;

    @Column(name = "option_d", nullable = false)
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    private String correctOption; // "A", "B", "C", or "D"

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "language", length = 100)
    private String language; // e.g. "Java", "Python", "JavaScript", "TypeScript", "SQL", "Go", "Rust"

    @Column(name = "framework", length = 100)
    private String framework; // e.g. "Spring Boot", "React", "Next.js", "Django", "Express", "Core"

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_type", length = 30)
    private TechType techType = TechType.LANGUAGE; // LANGUAGE, FRAMEWORK, LIBRARY, TOOL

    @Column(name = "tech_name", length = 100)
    private String techName; // e.g. "Java", "Spring Boot", "React", "Docker", "Pandas"

    public Question() {}

    public Question(Skill skill, String topic, String text, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation) {
        this.skill = skill;
        this.topic = topic;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.explanation = explanation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }

    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }

    public TechType getTechType() { return techType; }
    public void setTechType(TechType techType) { this.techType = techType; }

    public String getTechName() { return techName; }
    public void setTechName(String techName) { this.techName = techName; }
}
