package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.PortfolioItemCreateDTO;
import com.example.TalentOrbit.dto.response.PortfolioItemResponseDTO;
import com.example.TalentOrbit.dto.response.PortfolioVerificationResponseDTO;
import com.example.TalentOrbit.enums.PortfolioItemType;
import com.example.TalentOrbit.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    @Autowired private PortfolioService portfolioService;

    @PostMapping("/add")
    public ResponseEntity<PortfolioItemResponseDTO> addItem(@RequestBody PortfolioItemCreateDTO req) {
        return ResponseEntity.ok(portfolioService.addItem(req));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PortfolioItemResponseDTO>> getItems(
            @PathVariable Long userId,
            @RequestParam(required = false) PortfolioItemType type) {
        return ResponseEntity.ok(portfolioService.getItems(userId, type));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<PortfolioVerificationResponseDTO> verifyItem(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.verifyPortfolioItem(id));
    }
}
