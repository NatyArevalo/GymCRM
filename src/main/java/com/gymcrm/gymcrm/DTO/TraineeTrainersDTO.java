package com.gymcrm.gymcrm.DTO;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "Trainers for Trainee")
public class TraineeTrainersDTO {
    private Long traineeId;
    private TrainerDTO trainerDTO;

}
