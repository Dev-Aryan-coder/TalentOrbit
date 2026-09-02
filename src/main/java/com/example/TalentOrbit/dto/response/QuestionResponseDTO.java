package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.TechType;

public class QuestionResponseDTO {
    private Long id;
    private String topic;
    private String text;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String language;
    private String framework;
    private TechType techType;
    private String techName;

    public QuestionResponseDTO() {}

    public QuestionResponseDTO(Long id, String topic, String text, String optionA, String optionB, String optionC, String optionD) {
        this.id = id;
        this.topic = topic;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public QuestionResponseDTO(Long id, String topic, String text, String optionA, String optionB, String optionC, String optionD, String language, String framework, TechType techType, String techName) {
        this.id = id;
        this.topic = topic;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.language = language;
        this.framework = framework;
        this.techType = techType;
        this.techName = techName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }

    public TechType getTechType() { return techType; }
    public void setTechType(TechType techType) { this.techType = techType; }

    public String getTechName() { return techName; }
    public void setTechName(String techName) { this.techName = techName; }
}
