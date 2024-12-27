package com.gymcrm.gymcrm.DTO;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "Training Type")
public class TrainingTypeDTO {
    private String trainingTypeName;
}
