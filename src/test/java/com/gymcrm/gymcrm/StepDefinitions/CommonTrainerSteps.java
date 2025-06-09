package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class CommonTrainerSteps {
    private final TrainerContext trainerContext;

    public CommonTrainerSteps(TrainerContext trainerContext) {
        this.trainerContext = trainerContext;
    }

    @Given("a trainer with name {string} and lastname {string}")
    public void a_trainer_with_name_and_lastname(String name, String lastName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastName);
        trainerContext.getTrainerDTO().setUserDTO(userDTO);
    }
    @And("the specialization is {string}")
    public void the_specialization_is(String specialization) {
        trainerContext.getTrainerDTO().setSpecialization(new TrainingTypeDTO(specialization));
    }
}
