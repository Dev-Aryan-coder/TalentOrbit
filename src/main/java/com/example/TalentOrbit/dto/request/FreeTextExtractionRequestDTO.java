package com.example.TalentOrbit.dto.request;

public class FreeTextExtractionRequestDTO {
    private String freeText;

    public FreeTextExtractionRequestDTO() {}
    public FreeTextExtractionRequestDTO(String freeText) { this.freeText = freeText; }

    public String getFreeText() { return freeText; }
    public void setFreeText(String freeText) { this.freeText = freeText; }
}
