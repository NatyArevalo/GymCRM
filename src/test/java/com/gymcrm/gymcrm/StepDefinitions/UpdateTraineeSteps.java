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

import java.time.LocalDate;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class UpdateTraineeSteps {
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
    public UpdateTraineeSteps(TraineeContext traineeContext) {
        this.traineeContext = traineeContext;
    }
    @After
    public void cleanUp() {
        traineeContext.reset();
    }

    @Then("the trainee is created successfully")
    public void trainee_is_created_successfully() {
        traineeResult = traineeService.createTrainee(traineeContext.getTraineeDTO());
    }
    @When("the trainee with username {string} address is updated to {string}")
    public void the_trainee_with_username_address_is_updated_to(String username,String newAddress) {
        traineeContext.getTraineeDTO().setAddress(newAddress);
        traineeResult = traineeService.updateTrainee(username, traineeContext.getTraineeDTO());
    }
    @Then("the trainee's address should be updated successfully")
    public void the_trainees_address_should_be_updated_successfully() {
        assert traineeResult.getAddress().equals(traineeContext.getTraineeDTO().getAddress());
    }
    @When("the trainee with username {string} name is updated to {string}")
    public void the_trainee_with_username_name_is_updated_to(String username, String newName) {
        traineeContext.getTraineeDTO().getUserDTO().setFirstName(newName);
        traineeResult = traineeService.updateTrainee(username, traineeContext.getTraineeDTO());
    }
    @Then("the trainee's name should not be updated")
    public void the_trainees_name_should_not_be_updated() {
        assert !traineeResult.getUserDTO().getFirstName().equals(traineeContext.getTraineeDTO().getUserDTO().getFirstName());
    }
    @When("the trainee with username {string} date of birth is updated to {string}")
    public void the_trainee_with_username_date_of_birth_is_updated_to(String username, String newDateOfBirth) {
        traineeContext.getTraineeDTO().setDateOfBirth(LocalDate.parse(newDateOfBirth));
        traineeResult = traineeService.updateTrainee(username, traineeContext.getTraineeDTO());
    }
    @Then("the trainee's date of birth should not be updated")
    public void the_trainees_date_of_birth_should_not_be_updated() {
        assert !traineeResult.getDateOfBirth().equals(traineeContext.getTraineeDTO().getDateOfBirth());
    }
}
