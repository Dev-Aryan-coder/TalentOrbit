package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.Sender;
import java.time.LocalDateTime;

public class ChatMessageHistoryDTO {
    private Long id;
    private Sender sender;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    public ChatMessageHistoryDTO() {}

    public ChatMessageHistoryDTO(Long id, Sender sender, String content, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sender getSender() { return sender; }
    public void setSender(Sender sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
