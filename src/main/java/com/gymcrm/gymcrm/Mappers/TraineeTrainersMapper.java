package com.gymcrm.gymcrm.Mappers;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymcrm.gymcrm.DTO.TraineeTrainersDTO;
import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.TraineeTrainers;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TraineeTrainersMapper {
    
    ObjectMapper mapper;

    public TraineeTrainersDTO mapToDTO(TraineeTrainers traineeTrainers){
        UserDTO userDTO = mapper.convertValue(traineeTrainers.getTrainer().getUser(), UserDTO.class);
        return new TraineeTrainersDTO(traineeTrainers.getTrainee().getId(),
               new TrainerDTO(userDTO, mapper.convertValue(traineeTrainers.getTrainer().getSpecialization(), TrainingTypeDTO.class), null));
    }
}
