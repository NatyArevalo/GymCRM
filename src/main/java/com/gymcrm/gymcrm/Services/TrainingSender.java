package com.gymcrm.gymcrm.Services;

import com.gymcrm.gymcrm.DTO.TrainingBillingDTO;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainingSender {
    private final JmsTemplate jmsTemplate;

    public TrainingSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    public void sendTrainingData(TrainingBillingDTO trainingBillingDTO) {
        jmsTemplate.convertAndSend("trainer-billing-queue", trainingBillingDTO);
    }
}
