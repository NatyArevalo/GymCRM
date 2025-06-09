package com.gymcrm.gymcrm.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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

    @NotBlank(message = "Date of birth must not be blank")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth; //JSON "dateOfBirth": "2024-11-29"

    @NotBlank(message = "Address is required")
    private String Address;

    private List<TraineeTrainersDTO> traineeTrainersDTOs;
}
