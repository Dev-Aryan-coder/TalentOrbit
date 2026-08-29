package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.PortfolioItemType;

public class PortfolioItemResponseDTO {
    private Long id;
    private PortfolioItemType itemType;
    private String title;
    private String description;
    private String fileOrLink;
    private Boolean isVerified;

    public PortfolioItemResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PortfolioItemType getItemType() { return itemType; }
    public void setItemType(PortfolioItemType itemType) { this.itemType = itemType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFileOrLink() { return fileOrLink; }
    public void setFileOrLink(String fileOrLink) { this.fileOrLink = fileOrLink; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
}
