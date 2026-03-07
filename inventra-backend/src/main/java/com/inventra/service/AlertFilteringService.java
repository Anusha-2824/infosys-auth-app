package com.inventra.service;

import com.inventra.model.Alert;
import com.inventra.repository.AlertRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertFilteringService {

    private final AlertRepository alertRepository;

    public List<Alert> getFilteredAlerts(String type, String severity, Long productId, String status) {

        List<Alert> alerts = alertRepository.findAll();

        if (type != null)
            alerts = alertRepository.findByAlertType(type);

        if (severity != null)
            alerts = alertRepository.findBySeverity(severity);

        if (productId != null)
            alerts = alertRepository.findByProductId(productId);

        if (status != null)
            alerts = alertRepository.findByStatus(status);

        return alerts;
    }
}