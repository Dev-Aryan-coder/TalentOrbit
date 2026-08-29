package com.example.TalentOrbit.dto.request;

public class SkillCreateDTO {
    private String name;
    private String category;

    public SkillCreateDTO() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
