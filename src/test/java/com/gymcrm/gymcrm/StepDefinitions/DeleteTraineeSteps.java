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
public class DeleteTraineeSteps {
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
    public DeleteTraineeSteps(TraineeContext traineeContext) {
        this.traineeContext = traineeContext;
    }

    @After
    public void cleanUp() {
        traineeContext.reset();
    }

    @When("the trainee with username {string} and valid password is deleted")
    public void the_trainee_with_username_and_valid_password_is_deleted(String username) {
        traineeService.deleteTrainee(username, traineeContext.getTraineeDTO().getUserDTO().getPassword());
    }

    @Then("the trainee should be deleted successfully")
    public void the_trainee_should_be_deleted_successfully() {
        assert traineeRepository.searchTraineeByUsername(traineeContext.getTraineeDTO().getUserDTO().getUsername()) == null;
    }
    @When("an attempt is made to delete a trainee with username {string} and password {string}")
    public void an_attempt_is_made_to_delete_a_trainee_with_username_and_password(String username, String password) {
      traineeService.deleteTrainee(username, password);
    }
    @Then("the trainee should not be deleted")
    public void the_trainee_should_not_be_deleted() {
        assert traineeRepository.searchTraineeByUsername(traineeContext.getTraineeDTO().getUserDTO().getUsername()) != null;
    }
}
