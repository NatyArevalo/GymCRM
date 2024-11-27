package com.gymcrm.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gymcrm.Entities.Trainee;
import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.Training;
import com.gymcrm.Services.TraineeService;
import com.gymcrm.Services.TrainerService;
import com.gymcrm.Services.TrainingService;


@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public Trainer createTrainer(String firstName, String lastName, String isActive, String specialization) {
        return trainerService.createTrainer(firstName, lastName, isActive, specialization);
    }

    public Trainer updateTrainer(String username, String password, String isActive, String specialization) {
        return trainerService.updateTrainer(username, password, isActive, specialization);
    }

    public Trainer changePassword(String username, String password){
        return trainerService.changePassword(username, password);
    }

    public Trainer selectTrainer(String username) {
        return trainerService.selectTrainer(username);
    }

    public Trainee createTrainee(String firstname, String lastname, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address) {
        return traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
    }

    public Trainee updateTrainee(String username, String password, String isActive, String address) {
        return traineeService.updateTrainee(username, password, isActive, address);
    }

    public void deleteTrainee(String username, String password){
        traineeService.deleteTrainee(username, password);
    }

    public Trainee selectTrainee(String username) {
        return traineeService.selectTrainee(username);
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, String specialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double duration) {
        return trainingService.createTraining(traineeId, trainerId, trainingName, specialization, yearOfTraning, monthOfTraining, dayOfTraining, duration);
    }
}
