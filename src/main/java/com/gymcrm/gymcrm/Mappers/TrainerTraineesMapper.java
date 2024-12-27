package com.gymcrm.gymcrm.Mappers;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.TrainerTraineesDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.TraineeTrainers;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TrainerTraineesMapper {
    
    ObjectMapper mapper;

    public TrainerTraineesDTO mapToDTO(TraineeTrainers traineeTrainers){
        UserDTO userDTO = mapper.convertValue(traineeTrainers.getTrainee().getUser(), UserDTO.class);
        return new TrainerTraineesDTO(traineeTrainers.getTrainer().getId(),
               new TraineeDTO(userDTO, traineeTrainers.getTrainee().getDateOfBirth(), traineeTrainers.getTrainee().getAddress(),  null));
    }

}
