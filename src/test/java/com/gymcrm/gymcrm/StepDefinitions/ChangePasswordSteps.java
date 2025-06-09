package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.UserRepository;
import com.gymcrm.gymcrm.Services.TraineeService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class ChangePasswordSteps {
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
    public ChangePasswordSteps(TraineeContext traineeContext) {
        this.traineeContext = traineeContext;
    }
    @After
    public void cleanUp() {
        traineeContext.reset();
    }
    @When("the trainee with username {string} and password is changed to {string}")
    public void the_trainee_with_username_and_password_is_changed_to(String username, String newPassword) {
        traineeContext.getTraineeDTO().getUserDTO().setPassword(newPassword);
        traineeResult = traineeService.updateTrainee(username, traineeContext.getTraineeDTO());
    }
    @Then("the password should be updated successfully")
    public void the_password_should_be_updated_successfully() {
        assert traineeResult.getUserDTO().getPassword().equals(traineeContext.getTraineeDTO().getUserDTO().getPassword());
    }
    @When("the trainee with username {string} attempts to change the password with old password {string}")
    public void the_trainee_with_username_attempts_to_change_the_password_with_old_password(String username, String oldPassword) {
        traineeContext.getTraineeDTO().getUserDTO().setPassword(oldPassword);
        traineeResult = traineeService.updateTrainee(username, traineeContext.getTraineeDTO());
    }
    @Then("the returned result should be null")
    public void the_returned_result_should_be_null() {
        assert traineeResult == null;
    }
    @And("the password should not be changed")
    public void the_password_should_not_be_changed() {
        User user = userRepository.searchUserByUsername(traineeContext.getTraineeDTO().getUserDTO().getUsername());
        assert user.getPassword().equals(traineeContext.getTraineeDTO().getUserDTO().getPassword());
    }
}
