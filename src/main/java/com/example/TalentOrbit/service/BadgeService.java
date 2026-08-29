package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.BadgeResponseDTO;
import com.example.TalentOrbit.entity.Badge;
import com.example.TalentOrbit.entity.StudentBadge;
import com.example.TalentOrbit.repository.BadgeRepository;
import com.example.TalentOrbit.repository.StudentBadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BadgeService {
    @Autowired private BadgeRepository badgeRepository;
    @Autowired private StudentBadgeRepository studentBadgeRepository;

    public List<BadgeResponseDTO> getBadgesForStudent(Long userId) {
        List<Badge> allBadges = badgeRepository.findAll();
        List<StudentBadge> earned = studentBadgeRepository.findByUserId(userId);
        Map<Long, StudentBadge> earnedMap = new HashMap<>();
        for (StudentBadge sb : earned) {
            earnedMap.put(sb.getBadge().getId(), sb);
        }

        return allBadges.stream().map(b -> {
            BadgeResponseDTO dto = new BadgeResponseDTO();
            dto.setId(b.getId());
            dto.setName(b.getName());
            dto.setDescription(b.getDescription());
            dto.setIconUrl(b.getIconUrl());
            if (earnedMap.containsKey(b.getId())) {
                dto.setIsEarned(true);
                dto.setEarnedAt(earnedMap.get(b.getId()).getEarnedAt());
            } else {
                dto.setIsEarned(false);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
