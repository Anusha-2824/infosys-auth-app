package com.inventra.repository;

import com.inventra.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    boolean existsByProductIdAndAlertTypeAndStatus(Long productId, String alertType, String status);

    List<Alert> findByAlertType(String alertType);

    List<Alert> findBySeverity(String severity);

    List<Alert> findByProductId(Long productId);

    List<Alert> findByStatus(String status);
}