package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.TrainingDTO;
import com.gymcrm.gymcrm.Entities.Training;
import com.gymcrm.gymcrm.Repositories.TrainingRepository;
import com.gymcrm.gymcrm.Services.TrainingService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.Month;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class CreateTrainingSteps {
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingService trainingService;
    private Training trainingResult;
    private TrainingDTO trainingDTO;
    @When("a training request for trainer {string} by trainee {string} with new year {string} and month {string} and day {string} and a duration of {string}  with training name {string} is processed")
    public void a_valid_training_request(String trainerUsername, String traineeUsername, String year, String month, String day, String duration, String trainingName) {
        trainingDTO = new TrainingDTO();
        trainingDTO.setTrainerUsername(trainerUsername);
        trainingDTO.setTraineeUsername(traineeUsername);
        trainingDTO.setTrainingDate(LocalDate.of(Integer.parseInt(year),
                Month.valueOf(month.toUpperCase()).getValue(), Integer.parseInt(day)));
        trainingDTO.setDuration(Double.parseDouble(duration));
        trainingDTO.setTrainingName(trainingName);
    }
    @Then("a new training request should be created")
    public void a_new_training_request_should_be_created() {
        trainingResult = trainingService.createTraining(trainingDTO);
    }
    @And("the training should be saved and returned successfully")
    public void the_training_should_be_saved_and_returned_succesfully() {
        assert trainingResult != null;
        assert trainingResult.getTrainee().getUser().getUsername().equals(trainingDTO.getTraineeUsername());
        assert trainingResult.getTrainer().getUser().getUsername().equals(trainingDTO.getTrainerUsername());
        assert trainingResult.getTrainingDate().equals(trainingDTO.getTrainingDate());
        assert trainingResult.getDuration() == trainingDTO.getDuration();
        assert trainingResult.getTrainingName().equals(trainingDTO.getTrainingName());
    }
}
