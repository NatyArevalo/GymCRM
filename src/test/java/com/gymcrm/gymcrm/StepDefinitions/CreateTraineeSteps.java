package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.UserRepository;
import com.gymcrm.gymcrm.Services.TraineeService;
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
public class CreateTraineeSteps {
    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDTO userDTO;
    private TraineeDTO traineeResult;
    private final TraineeContext traineeContext;

    @Autowired
    public CreateTraineeSteps(TraineeContext traineeContext) {
        this.traineeContext = traineeContext;
    }

    @After
    public void cleanUp() {
        traineeContext.reset();
    }



    @When("trainee is created")
    public void trainee_is_created() {
        traineeResult = traineeService.createTrainee(traineeContext.getTraineeDTO());
    }

    @Then("the trainee should be created successfully")
    public void the_trainee_should_be_created_successfully_with_role() {
        assert traineeResult != null;
        assert traineeResult.getUserDTO().getFirstName().equals(traineeContext.getTraineeDTO().getUserDTO().getFirstName());
        assert traineeResult.getUserDTO().getLastName().equals(traineeContext.getTraineeDTO().getUserDTO().getLastName());
        assert traineeResult.getUserDTO().getUsername().equals(traineeContext.getTraineeDTO().getUserDTO().getUsername());
        assert traineeResult.getUserDTO().getIsActive() == traineeContext.getTraineeDTO().getUserDTO().getIsActive();
        assert traineeResult.getDateOfBirth().equals(traineeContext.getTraineeDTO().getDateOfBirth());
        assert traineeResult.getAddress().equals(traineeContext.getTraineeDTO().getAddress());
    }
    @When("trainee creation is attempted")
    public void trainee_creation_is_attempted() {
        traineeResult = traineeService.createTrainee(traineeContext.getTraineeDTO());
    }
    @Then("trainee creation should fail and result should be null")
    public void trainee_creation_should_fail_and_result_should_be_null() {
        assert traineeResult == null;
    }


}
