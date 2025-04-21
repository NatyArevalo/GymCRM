package com.gymcrm.gymcrm.Breaker;

import com.gymcrm.gymcrm.Client.TrainerBillingClient;
import com.gymcrm.gymcrm.DTO.TrainingBillingDTO;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;

public class TrainerBillingFallback implements FallbackFactory<TrainerBillingClient> {

    @Override
    public TrainerBillingClient create(Throwable cause) {
        if (cause instanceof FeignException fe) {
            int status = fe.status();
            System.out.println("Global fallback: HTTP Status from billing = " + status);
        }
        return new TrainerBillingClient() {

            @Override
            public void notifyTrainingCreated(TrainingBillingDTO trainingBillingDTO) {
                System.err.println("Fallback: notifyTrainingCreated failed");

                // Per-method error inspection (more fine-grained)
                if (cause instanceof FeignException fe) {
                    int status = fe.status();
                    System.out.println("notifyTrainingCreated fallback - HTTP Status: " + status);

                }
            }
        };
    }
}
