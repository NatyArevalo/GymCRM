package com.gymcrm.gymcrm.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.gymcrm.DTO.TrainingDTO;
import com.gymcrm.gymcrm.Entities.Training;
import com.gymcrm.gymcrm.Entities.TrainingType;
import com.gymcrm.gymcrm.Mappers.TrainingMapper;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.TrainingRepository;


@Service
public class TrainingService {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    TraineeRepository traineeRepository;

    @Autowired
    TrainerRepository trainerRepository;

    @Autowired
    TrainingMapper trainingMapper;

    public Training createTraining(TrainingDTO trainingDTO){
        Training training = new Training();
        try {
            if (traineeRepository.searchTraineeByUsername(trainingDTO.getTraineeUsername())==null) {
                throw new Exception("Trainee name cannot be empty or null");
            }else{
                training.setTrainee(traineeRepository.searchTraineeByUsername(trainingDTO.getTraineeUsername()));
            }
            if (trainerRepository.searchTrainerByUsername(trainingDTO.getTrainerUsername())==null) {
                throw new Exception("Trainer name cannot be empty or null");
            }else{
                training.setTrainer(trainerRepository.searchTrainerByUsername(trainingDTO.getTrainerUsername()));
            }

            if (trainingDTO.getTrainingName() == null || trainingDTO.getTrainingName().trim().isEmpty()) {
                throw new Exception("Name of training cannot be empty");
            }
            if (trainingDTO.getTrainingTypeName() == null) {
                throw new Exception("Training type name cannot be null");
            }
            if (trainingDTO.getTrainingDate() == null) {
                throw new Exception("Training Date fields cannot be empty");
            }
            if (trainingDTO.getDuration() == null) {
                throw new Exception("Duration of Training cannot be empty or null");
            }
            
            training.setTrainingName(trainingDTO.getTrainingName());

            if (!trainingDTO.getTrainingTypeName().toString().trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(trainerRepository.searchTrainingTypeByName(trainingDTO.getTrainingTypeName().getTrainingTypeName()));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    training.setTrainingType(trainingType);
                }    
            }else{
                throw new Exception("specialization cannot be empty");
            }
            
            training.setTrainingDate(trainingDTO.getTrainingDate());
            training.setDuration(trainingDTO.getDuration());

        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        trainingRepository.save(training);
        return training;
    }

    public List<TrainingDTO> getTraineeTrainings(String username, LocalDate dateFrom, LocalDate dateTo, String trainerUsername, String traininType){
       try {
            if(username == null || username.trim().isEmpty()){
                throw new Exception("Trainee username cannot be empty or null");
            }
            List<Training> traineeTrainings = new ArrayList<>();
            List<TrainingDTO> traineeTrainingDTOs = new ArrayList<>();
            traineeTrainings.addAll(trainingRepository.searchTraineeTrainingsByCriteria(username, dateFrom, dateTo, trainerUsername, traininType));
            for(Training traineeTraining :  traineeTrainings){
                TrainingDTO traineeTrainingDTO = new TrainingDTO();
                traineeTrainingDTO = trainingMapper.mapToDTO(traineeTraining);
                traineeTrainingDTOs.add(traineeTrainingDTO);
            }
            return traineeTrainingDTOs;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        
    }

    public List<TrainingDTO> getTrainerTrainings(String username, LocalDate dateFrom, LocalDate dateTo, String traineeUsername){
        try {
             if(username == null || username.trim().isEmpty()){
                 throw new Exception("Trainer username cannot be empty or null");
             }
             List<Training> trainerTrainings = new ArrayList<>();
             List<TrainingDTO> trainerTrainingDTOs = new ArrayList<>();
             trainerTrainings.addAll(trainingRepository.searchTrainerTrainingsByCriteria(username, dateFrom, dateTo, traineeUsername));
             for(Training trainerTraining :  trainerTrainings){
                 TrainingDTO trainerTrainingDTO = new TrainingDTO();
                 trainerTrainingDTO = trainingMapper.mapToDTO(trainerTraining);
                 trainerTrainingDTOs.add(trainerTrainingDTO);
             }
             return trainerTrainingDTOs;
 
         } catch (Exception e){
             System.out.println(e.getMessage());
             return null;
         }
         
     }
}
