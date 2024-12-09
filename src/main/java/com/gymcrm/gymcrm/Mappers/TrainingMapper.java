package com.gymcrm.gymcrm.Mappers;

import org.springframework.stereotype.Component;

import com.gymcrm.gymcrm.DTO.TrainingDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.Entities.Training;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TrainingMapper {

    public TrainingDTO mapToDTO(Training training){
        return new TrainingDTO(training.getTrainingName(), new TrainingTypeDTO(training.getTrainingType().toString()), training.getTrainingDate(),
                    training.getDuration(), training.getTrainee().getUser().getUsername(), training.getTrainer().getUser().getUsername());
    }

}
