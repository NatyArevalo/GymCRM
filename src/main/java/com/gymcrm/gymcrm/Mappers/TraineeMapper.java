package com.gymcrm.gymcrm.Mappers;

import org.springframework.stereotype.Component;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.Entities.Trainee;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Component
@AllArgsConstructor
@NoArgsConstructor
public class TraineeMapper {

    UserMapper userMapper;
    TraineeTrainersMapper traineeTrainersMapper;

    public TraineeDTO mapToDTO(Trainee trainee){
        return new TraineeDTO(userMapper.mapToDTO(trainee.getUser()), trainee.getDateOfBirth(), trainee.getAddress(), trainee.getTraineeTrainers().stream().map(traineeTrainersMapper::mapToDTO).toList());
    }
}
