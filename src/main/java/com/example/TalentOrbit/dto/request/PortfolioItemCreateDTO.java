package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.PortfolioItemType;

public class PortfolioItemCreateDTO {
    private Long userId;
    private PortfolioItemType itemType;
    private String title;
    private String description;
    private String fileOrLink;

    public PortfolioItemCreateDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public PortfolioItemType getItemType() { return itemType; }
    public void setItemType(PortfolioItemType itemType) { this.itemType = itemType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFileOrLink() { return fileOrLink; }
    public void setFileOrLink(String fileOrLink) { this.fileOrLink = fileOrLink; }
}
