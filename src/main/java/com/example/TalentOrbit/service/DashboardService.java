package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.DashboardStatsResponseDTO;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.repository.ApplicationRepository;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired private UserRepository userRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private ApplicationRepository applicationRepository;

    public DashboardStatsResponseDTO getStatsForRole(Role role) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalUsers", userRepository.count());
        map.put("activePostings", postingRepository.findByIsActiveTrue().size());
        map.put("totalApplications", applicationRepository.count());
        map.put("role", role);
        return new DashboardStatsResponseDTO(map);
    }
}
