package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainerContext {
    private TrainerDTO trainerDTO = new TrainerDTO();
    public TrainerDTO getTrainerDTO() {
        return trainerDTO;
    }
    public void setTrainerDTO(TrainerDTO trainerDTO) {
        this.trainerDTO = trainerDTO;
    }
    public void reset() {
        this.trainerDTO = new TrainerDTO();
    }
}
