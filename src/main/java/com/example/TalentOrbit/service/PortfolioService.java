package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.PortfolioItemCreateDTO;
import com.example.TalentOrbit.dto.response.PortfolioItemResponseDTO;
import com.example.TalentOrbit.entity.Portfolio;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.PortfolioItemType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.PortfolioRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    @Autowired private PortfolioRepository portfolioRepository;
    @Autowired private UserRepository userRepository;

    public PortfolioItemResponseDTO addItem(PortfolioItemCreateDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Portfolio item = new Portfolio();
        item.setUser(user);
        item.setItemType(req.getItemType());
        item.setTitle(req.getTitle());
        item.setDescription(req.getDescription());
        item.setFileOrLink(req.getFileOrLink());
        item.setIsVerified(false);
        Portfolio saved = portfolioRepository.save(item);

        PortfolioItemResponseDTO dto = new PortfolioItemResponseDTO();
        dto.setId(saved.getId());
        dto.setItemType(saved.getItemType());
        dto.setTitle(saved.getTitle());
        dto.setDescription(saved.getDescription());
        dto.setFileOrLink(saved.getFileOrLink());
        dto.setIsVerified(saved.getIsVerified());
        return dto;
    }

    public List<PortfolioItemResponseDTO> getItems(Long userId, PortfolioItemType type) {
        List<Portfolio> list;
        if (type != null) {
            list = portfolioRepository.findByUserIdAndItemType(userId, type);
        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            list = portfolioRepository.findByUser(user);
        }
        return list.stream().map(p -> {
            PortfolioItemResponseDTO dto = new PortfolioItemResponseDTO();
            dto.setId(p.getId());
            dto.setItemType(p.getItemType());
            dto.setTitle(p.getTitle());
            dto.setDescription(p.getDescription());
            dto.setFileOrLink(p.getFileOrLink());
            dto.setIsVerified(p.getIsVerified());
            return dto;
        }).collect(Collectors.toList());
    }
}
