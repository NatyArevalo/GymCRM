package com.gymcrm.gymcrm.DTO;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "Trainees for Trainer")
public class TrainerTraineesDTO {
    private Long trainerId;
    private TraineeDTO traineeDTO;
}
