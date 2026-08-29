package com.example.TalentOrbit.dto.response;

public class QuestionResponseDTO {
    private Long id;
    private String topic;
    private String text;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

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
}
