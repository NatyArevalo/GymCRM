package com.gymcrm.gymcrm.DTO;

import com.gymcrm.gymcrm.Enumerators.ActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingBillingDTO implements Serializable {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private Boolean isTrainerActive;
    private LocalDate trainingDate;
    private Double trainingDuration;
    private ActionType actionType;
}
