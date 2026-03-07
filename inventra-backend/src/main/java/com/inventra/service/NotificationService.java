package com.inventra.service;

import com.inventra.model.Alert;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendAlert(Alert alert) {

        System.out.println("ALERT GENERATED: " + alert.getMessage());

        // Future upgrade
        // send email
        // send SMS
        // push dashboard notification
    }
}