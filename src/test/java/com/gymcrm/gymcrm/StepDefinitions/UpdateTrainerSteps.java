package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.UserRepository;
import com.gymcrm.gymcrm.Services.TrainerService;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class UpdateTrainerSteps {
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;
    private UserDTO userDTO;
    private TrainerDTO trainerResult;
    private final TrainerContext trainerContext;
    @Autowired
    public UpdateTrainerSteps(TrainerContext trainerContext) {
        this.trainerContext = trainerContext;
    }
    @After
    public void cleanUp() {
        trainerContext.reset();
    }
    @Then("the trainer should be created")
    public void trainer_is_created() {
        trainerResult = trainerService.createTrainer(trainerContext.getTrainerDTO());
    }
    @When("the trainer with username {string} the specialization is updated to {string}")
    public void the_trainer_with_username_the_specialization_is_updated_to(String username, String newSpecialization) {
        trainerContext.getTrainerDTO().setSpecialization(new TrainingTypeDTO(newSpecialization));
        trainerResult = trainerService.updateTrainer(username, trainerContext.getTrainerDTO());
    }
    @Then("the trainer's specialization should be updated successfully")
    public void the_trainers_specialization_should_be_updated_successfully() {
        assert trainerResult.getSpecialization().getTrainingTypeName().equals(trainerContext.getTrainerDTO().getSpecialization().getTrainingTypeName());
    }
    @When("the trainer with username {string} name is updated to {string}")
    public void the_trainer_with_username_name_is_updated_to(String username, String newName) {
        trainerContext.getTrainerDTO().getUserDTO().setFirstName(newName);
        trainerResult = trainerService.updateTrainer(username, trainerContext.getTrainerDTO());
    }
    @Then("the trainer's name should not be updated")
    public void the_trainers_name_should_not_be_updated() {
        assert !trainerResult.getUserDTO().getFirstName().equals(trainerContext.getTrainerDTO().getUserDTO().getFirstName());
    }

    @When("the trainer with username {string} is updated with specialization {string}")
    public void the_trainer_with_username_is_updated_with_specialization(String username, String specialization) {
        trainerContext.getTrainerDTO().setSpecialization(new TrainingTypeDTO(specialization));
        trainerResult = trainerService.updateTrainer(username, trainerContext.getTrainerDTO());
    }
    @Then("the trainer's specialization should not be updated")
    public void the_trainers_specialization_should_not_be_updated() {
        assert !trainerResult.getSpecialization().getTrainingTypeName().equals(trainerContext.getTrainerDTO().getSpecialization().getTrainingTypeName());
    }
}
