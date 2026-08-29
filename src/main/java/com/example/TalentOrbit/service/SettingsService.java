package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.PlatformSettingUpdateDTO;
import com.example.TalentOrbit.dto.response.PlatformSettingResponseDTO;
import com.example.TalentOrbit.entity.PlatformSetting;
import com.example.TalentOrbit.repository.PlatformSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SettingsService {
    @Autowired private PlatformSettingRepository settingRepository;

    public void updateSetting(PlatformSettingUpdateDTO req) {
        PlatformSetting ps = settingRepository.findBySettingKey(req.getSettingKey())
                .orElse(new PlatformSetting());
        ps.setSettingKey(req.getSettingKey());
        ps.setSettingValue(req.getSettingValue());
        settingRepository.save(ps);
    }

    public List<PlatformSettingResponseDTO> getAllSettings() {
        return settingRepository.findAll().stream().map(ps -> {
            PlatformSettingResponseDTO dto = new PlatformSettingResponseDTO();
            dto.setId(ps.getId());
            dto.setSettingKey(ps.getSettingKey());
            dto.setSettingValue(ps.getSettingValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
