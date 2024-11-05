package com.gymcrm.Services;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Entities.Training;
import com.gymcrm.Entities.TrainingType;
import com.gymcrm.Persistence.DaoTraineeImpl;
import com.gymcrm.Persistence.DaoTrainerImpl;
import com.gymcrm.Persistence.DaoTrainingImpl;

@Service
public class TrainingService {
    private final DaoTrainingImpl daoTraining;
    private final DaoTraineeImpl daoTrainee;
    private final DaoTrainerImpl daoTrainer;
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    public TrainingService(DaoTrainingImpl daoTraining, DaoTraineeImpl daoTrainee, DaoTrainerImpl daoTrainer){
        this.daoTraining = daoTraining;
        this.daoTrainee = daoTrainee;
        this.daoTrainer = daoTrainer;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, String specialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double duration){
        Training training = new Training();
        try {
            if (trainerId == null) {
                throw new Exception("ID of Trainer cannot be empty or null");
            }

            if (traineeId == null) {
                throw new Exception("ID of Trainee cannot be empty or null");
            }

            if (trainingName == null || trainingName.trim().isEmpty()) {
                throw new Exception("Name of training cannot be empty");
            }
            if (specialization == null || specialization.trim().isEmpty()) {
                throw new Exception("Training type name cannot be empty");
            }
            if (yearOfTraning == null || monthOfTraining == null || dayOfTraining == null) {
                throw new Exception("Training Date fields cannot be empty");
            }
            if (duration == null) {
                throw new Exception("Duration of Training cannot be empty or null");
            }
            
            if(daoTrainer.getTrainerById(trainerId)==null){
                throw new Exception("ID of Trainer needs to be valid");
            }else{
                training.setTrainer(daoTrainer.getTrainerById(trainerId));
            }
            if(daoTrainee.getTraineeById(traineeId)==null){
                throw new Exception("ID of Trainee needs to be valid");
            }else{
                training.setTrainee(daoTrainee.getTraineeById(traineeId));
            }

            training.setTrainingName(trainingName);

            if (!specialization.trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(daoTrainer.searchTrainingTypeByName(specialization));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    training.setTrainingType(trainingType);
                }    
            }else{
                throw new Exception("specialization cannot be empty");
            }
            
            training.setTrainingDate(LocalDate.of(yearOfTraning, monthOfTraining, dayOfTraining));
            training.setDuration(duration);

        } catch (Exception e){
            logger.error("Failed to create Training", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTraining.create(training);
        logger.info("Training created successfully: {}", training);
        return training;
    }    
}
