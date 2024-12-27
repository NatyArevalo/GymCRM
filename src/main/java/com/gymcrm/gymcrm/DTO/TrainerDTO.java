package com.gymcrm.gymcrm.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "TrainerInfo")
public class TrainerDTO {
    private UserDTO userDTO;
    private TrainingTypeDTO specialization;
    private List<TrainerTraineesDTO> trainerTraineesDTOs;
}
