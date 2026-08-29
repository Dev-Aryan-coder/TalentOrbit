package com.example.TalentOrbit.dto.response;

public class PortfolioVerificationResponseDTO {
    private Long portfolioId;
    private String title;
    private boolean isTamperFree;
    private String currentHash;
    private String storedHash;
    private String statusMessage;

    public PortfolioVerificationResponseDTO() {}

    public PortfolioVerificationResponseDTO(Long portfolioId, String title, boolean isTamperFree, String currentHash, String storedHash, String statusMessage) {
        this.portfolioId = portfolioId;
        this.title = title;
        this.isTamperFree = isTamperFree;
        this.currentHash = currentHash;
        this.storedHash = storedHash;
        this.statusMessage = statusMessage;
    }

    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isTamperFree() { return isTamperFree; }
    public void setTamperFree(boolean tamperFree) { isTamperFree = tamperFree; }

    public String getCurrentHash() { return currentHash; }
    public void setCurrentHash(String currentHash) { this.currentHash = currentHash; }

    public String getStoredHash() { return storedHash; }
    public void setStoredHash(String storedHash) { this.storedHash = storedHash; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
