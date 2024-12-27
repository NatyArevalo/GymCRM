package com.gymcrm.gymcrm.Mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.Entities.Trainer;

@Component
public class TrainerMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TrainerTraineesMapper trainerTraineesMapper;

     public TrainerDTO mapToDTO(Trainer trainer){
        return new TrainerDTO(userMapper.mapToDTO(trainer.getUser()), new TrainingTypeDTO(trainer.getSpecialization().toString()), trainer.getTrainerTrainees().stream().map(trainerTraineesMapper::mapToDTO).toList());
    }
}
