package com.gymcrm.gymcrm.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "TrainingInfo")
public class TrainingDTO {
    private String trainingName;
    private TrainingTypeDTO trainingTypeName;
    private LocalDate trainingDate; //JSON "trainingDate": "2024-11-29"
    private Double duration;
    private String traineeUsername;
    private String trainerUsername;
}
