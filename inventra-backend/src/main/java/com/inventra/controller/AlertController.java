package com.inventra.controller;

import com.inventra.model.Alert;
import com.inventra.repository.AlertRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    // Get all alerts
    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }

    // High severity alerts
    @GetMapping("/high")
    public List<Alert> getHighAlerts() {
        return alertRepository.findBySeverity("HIGH");
    }

    // Medium severity alerts
    @GetMapping("/medium")
    public List<Alert> getMediumAlerts() {
        return alertRepository.findBySeverity("MEDIUM");
    }

    // Resolved alerts
    @GetMapping("/resolved")
    public List<Alert> getResolvedAlerts() {
        return alertRepository.findByStatus("RESOLVED");
    }
}