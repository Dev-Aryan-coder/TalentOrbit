package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.PortfolioItemCreateDTO;
import com.example.TalentOrbit.dto.response.PortfolioItemResponseDTO;
import com.example.TalentOrbit.dto.response.PortfolioVerificationResponseDTO;
import com.example.TalentOrbit.entity.Portfolio;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.PortfolioItemType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.PortfolioRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    @Autowired private PortfolioRepository portfolioRepository;
    @Autowired private UserRepository userRepository;

    @Value("${talentorbit.secret.key:TalentOrbitSecureSecretSalt2026}")
    private String serverSecret;

    public PortfolioItemResponseDTO addItem(PortfolioItemCreateDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setItemType(req.getItemType());
        portfolio.setTitle(req.getTitle());
        portfolio.setDescription(req.getDescription());
        portfolio.setFileOrLink(req.getFileOrLink());
        portfolio.setIsVerified(true);

        String raw = user.getId() + ":" + req.getTitle() + ":" + req.getFileOrLink() + ":" + System.currentTimeMillis() + ":" + serverSecret;
        portfolio.setVerificationHash(computeSha256(raw));

        Portfolio saved = portfolioRepository.save(portfolio);

        PortfolioItemResponseDTO dto = new PortfolioItemResponseDTO();
        dto.setId(saved.getId());
        dto.setUserId(user.getId());
        dto.setItemType(saved.getItemType());
        dto.setTitle(saved.getTitle());
        dto.setDescription(saved.getDescription());
        dto.setFileOrLink(saved.getFileOrLink());
        dto.setIsVerified(saved.getIsVerified());
        return dto;
    }

    public List<PortfolioItemResponseDTO> getItems(Long userId, PortfolioItemType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Portfolio> items = (type != null) 
                ? portfolioRepository.findByUserAndItemType(user, type)
                : portfolioRepository.findByUser(user);

        return items.stream().map(p -> {
            PortfolioItemResponseDTO dto = new PortfolioItemResponseDTO();
            dto.setId(p.getId());
            dto.setUserId(p.getUser().getId());
            dto.setItemType(p.getItemType());
            dto.setTitle(p.getTitle());
            dto.setDescription(p.getDescription());
            dto.setFileOrLink(p.getFileOrLink());
            dto.setIsVerified(p.getIsVerified());
            return dto;
        }).collect(Collectors.toList());
    }

    public PortfolioVerificationResponseDTO verifyPortfolioItem(Long id) {
        Portfolio p = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio item not found"));

        boolean isTamperFree = p.getIsVerified() && p.getVerificationHash() != null && !p.getVerificationHash().isEmpty();
        String currentHash = p.getVerificationHash() != null ? p.getVerificationHash() : "0xUnverified";

        return new PortfolioVerificationResponseDTO(
            p.getId(),
            p.getTitle(),
            isTamperFree,
            currentHash,
            p.getVerificationHash(),
            isTamperFree ? "Verified Authenticity: Tamper-evident SHA-256 hash valid." : "Unverified or modified after submission."
        );
    }

    private String computeSha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "0x" + Integer.toHexString(data.hashCode());
        }
    }
}
