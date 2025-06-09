package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.UserRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import com.gymcrm.gymcrm.Services.TrainerService;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class CreateTrainerSteps {
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
    public CreateTrainerSteps(TrainerContext trainerContext) {
        this.trainerContext = trainerContext;
    }
    @After
    public void cleanUp() {
        trainerContext.reset();
    }
    @When("trainer is created")
    public void trainer_is_created() {
        trainerResult = trainerService.createTrainer(trainerContext.getTrainerDTO());
    }
    @Then("the trainer should be created successfully")
    public void the_trainer_should_be_created_successfully() {
        assert trainerResult != null;
        assert trainerResult.getUserDTO().getFirstName().equals(trainerContext.getTrainerDTO().getUserDTO().getFirstName());
        assert trainerResult.getUserDTO().getLastName().equals(trainerContext.getTrainerDTO().getUserDTO().getLastName());
        assert trainerResult.getUserDTO().getUsername().equals(trainerContext.getTrainerDTO().getUserDTO().getUsername());
        assert trainerResult.getUserDTO().getIsActive() == trainerContext.getTrainerDTO().getUserDTO().getIsActive();
        assert trainerResult.getSpecialization().equals(trainerContext.getTrainerDTO().getSpecialization());
    }
    @When("trainer creation is attempted")
    public void trainer_creation_is_attempted() {
        trainerResult = trainerService.createTrainer(trainerContext.getTrainerDTO());
    }
    @Then("trainer creation should fail and result should be null")
    public void trainer_creation_should_fail_and_result_should_be_null() {
        assert trainerResult == null;
    }


}
