package com.example.TalentOrbit.dto.response;

public class SkillPreviewResponseDTO {
    private int allMatchCount;
    private int anyMatchCount;

    public SkillPreviewResponseDTO() {}
    public SkillPreviewResponseDTO(int allMatchCount, int anyMatchCount) {
        this.allMatchCount = allMatchCount;
        this.anyMatchCount = anyMatchCount;
    }

    public int getAllMatchCount() { return allMatchCount; }
    public void setAllMatchCount(int allMatchCount) { this.allMatchCount = allMatchCount; }

    public int getAnyMatchCount() { return anyMatchCount; }
    public void setAnyMatchCount(int anyMatchCount) { this.anyMatchCount = anyMatchCount; }
}
