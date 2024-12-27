package com.gymcrm.gymcrm.Configurations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


@Component
//http://localhost:8080/actuator/health/healthIndicators
public class HealthIndicators implements  HealthIndicator{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Health health() {
        boolean isHealthy = checkDatabaseHealth();

        if (isHealthy) {
            return Health.up().build();
        } else {
            return Health.down()
                    .withDetail("reason", "Database health check failed")
                    .build();
        }
    }

    private boolean checkDatabaseHealth() {
        try {
            entityManager.createQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            System.err.println("Database health check failed: " + e.getMessage());
        }
        return false;
    }
}
