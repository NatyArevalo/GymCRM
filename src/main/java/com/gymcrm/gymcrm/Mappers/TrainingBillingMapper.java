package com.gymcrm.gymcrm.Mappers;

import com.gymcrm.gymcrm.DTO.TrainingBillingDTO;
import com.gymcrm.gymcrm.Entities.Training;
import com.gymcrm.gymcrm.Enumerators.ActionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class TrainingBillingMapper {
    public TrainingBillingDTO mapToDTO(Training training){
        TrainingBillingDTO trainingBillingDTO = new TrainingBillingDTO();
        trainingBillingDTO.setTrainerUsername(training.getTrainer().getUser().getUsername());
        trainingBillingDTO.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
        trainingBillingDTO.setTrainerLastName(training.getTrainer().getUser().getLastName());
        trainingBillingDTO.setIsTrainerActive(training.getTrainer().getUser().getIsActive());
        trainingBillingDTO.setTrainingDate(training.getTrainingDate());
        trainingBillingDTO.setTrainingDuration(training.getDuration());
        trainingBillingDTO.setActionType(ActionType.ADD);
        return trainingBillingDTO;
    }
}
