package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.PlatformSettingUpdateDTO;
import com.example.TalentOrbit.dto.response.PlatformSettingResponseDTO;
import com.example.TalentOrbit.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    @Autowired private SettingsService settingsService;

    @PutMapping("/update")
    public ResponseEntity<String> updateSetting(@RequestBody PlatformSettingUpdateDTO req) {
        settingsService.updateSetting(req);
        return ResponseEntity.ok("Setting updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlatformSettingResponseDTO>> getAllSettings() {
        return ResponseEntity.ok(settingsService.getAllSettings());
    }
}
