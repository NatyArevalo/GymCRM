package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.*;
import com.gymcrm.gymcrm.Entities.AuthenticationResponse;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.TrainingRepository;
import com.gymcrm.gymcrm.Services.AuthenticationService;
import com.gymcrm.gymcrm.Services.TraineeService;
import com.gymcrm.gymcrm.Services.TrainerService;
import com.gymcrm.gymcrm.Services.TrainingService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class FullFlowIntegrationSteps {
    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private TraineeDTO createdTrainee;
    private TrainerDTO createdTrainer;
    private TrainingDTO createdTraining;

    @Given("a trainee {string} with lastname {string} date of birth {string} and address {string}")
    public void createTrainee(String name, String lastname, String birthday,String address) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastname);
        TraineeDTO dto = new TraineeDTO(userDTO, LocalDate.parse(birthday), address, new ArrayList<>());
        createdTrainee = traineeService.createTrainee(dto);
        assertNotNull(createdTrainee);
    }

    @And("a trainer {string} with lastname {string} and specialization {string}")
    public void createTrainer(String name, String lastname, String specialization) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastname);
        TrainerDTO dto = new TrainerDTO(userDTO, new TrainingTypeDTO(specialization), new ArrayList<>());
        createdTrainer = trainerService.createTrainer(dto);
        assertNotNull(createdTrainer);
    }

    @When("trainee logs in with valid password")
    public void login() {
        AuthenticationResponse loginResult = authService.authenticate(createdTrainee.getUserDTO());
        assertNotNull(loginResult);
    }

    @And("a training request is created for trainer {string} by trainee {string} with new year {string} and month {string} and day {string} and a duration of {string}  with training name {string} is processed")
    public void a_valid_training_request(String trainerUsername, String traineeUsername, String year, String month, String day, String duration, String trainingName) {
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setTrainerUsername(trainerUsername);
        trainingDTO.setTraineeUsername(traineeUsername);
        trainingDTO.setTrainingDate(LocalDate.of(Integer.parseInt(year),
                Month.valueOf(month.toUpperCase()).getValue(), Integer.parseInt(day)));
        trainingDTO.setDuration(Double.parseDouble(duration));
        trainingDTO.setTrainingName(trainingName);
    }

    @Then("the training should exist for trainee {string}")
    public void verifyTraining(String traineeUsername) {
        List<TrainingDTO> trainings = trainingService.getTraineeTrainings(traineeUsername, null, null, null, null);
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingName().equals(createdTraining.getTrainingName())));
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainerUsername().equals(createdTraining.getTrainerUsername())));
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingDate().equals(createdTraining.getTrainingDate())));
    }

}
