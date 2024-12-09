package com.gymcrm.gymcrm.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "TraineeInfo")
public class TraineeDTO {
    private UserDTO userDTO;
    private LocalDate dateOfBirth; //JSON "dateOfBirth": "2024-11-29" 
    private String Address;
    private List<TraineeTrainersDTO> traineeTrainersDTOs;
}
