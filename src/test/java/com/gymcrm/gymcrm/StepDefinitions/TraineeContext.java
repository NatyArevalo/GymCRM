package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import org.springframework.stereotype.Component;

@Component
public class TraineeContext {
    private TraineeDTO traineeDTO = new TraineeDTO();

    public TraineeDTO getTraineeDTO() {
        return traineeDTO;
    }

    public void setTraineeDTO(TraineeDTO traineeDTO) {
        this.traineeDTO = traineeDTO;
    }

    public void reset() {
        this.traineeDTO = new TraineeDTO();
    }
}
