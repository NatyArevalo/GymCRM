package com.gymcrm.gymcrm.Client;

import com.gymcrm.gymcrm.Breaker.TrainerBillingFallback;
import com.gymcrm.gymcrm.Configurations.FeignClientConfig;
import com.gymcrm.gymcrm.DTO.TrainingBillingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "trainerbilling", path = "/trainerbilling",fallback = TrainerBillingFallback.class, configuration = FeignClientConfig.class)
public interface TrainerBillingClient {
    @PostMapping("/billing")
    void notifyTrainingCreated(@RequestBody TrainingBillingDTO trainingBillingDTO);

}
