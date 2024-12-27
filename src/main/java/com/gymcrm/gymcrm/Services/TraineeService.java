package com.gymcrm.gymcrm.Services;

import java.util.ArrayList;
import java.util.List;

import com.gymcrm.gymcrm.Enumerators.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.TraineeTrainersDTO;
import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.Trainee;
import com.gymcrm.gymcrm.Entities.TraineeTrainers;
import com.gymcrm.gymcrm.Entities.Trainer;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Mappers.TraineeTrainersMapper;
import com.gymcrm.gymcrm.Mappers.TrainerMapper;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;


@Service
public class TraineeService {

    @Autowired
    UserService userService;

    @Autowired
    TraineeRepository traineeRepository;

    @Autowired
    TrainerRepository trainerRepository;

    @Autowired
    TraineeTrainersMapper traineeTrainersMapper;

    @Autowired
    TrainerMapper trainerMapper;


    public TraineeDTO createTrainee(TraineeDTO traineeDTO){
        Trainee trainee = new Trainee();
        try {
            trainee.setUser(userService.createUser(traineeDTO.getUserDTO()));
            trainee.getUser().setRol(Rol.TRAINEE);
            if (traineeDTO.getDateOfBirth() == null) {
                throw new Exception("Birth fields cannot be empty");
            }

            trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
            trainee.setAddress(traineeDTO.getAddress());

        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        traineeRepository.save(trainee);
        return new TraineeDTO(new UserDTO(null, null, trainee.getUser().getUsername(), trainee.getUser().getPassword(),null),null, null,null);  
    }

    public TraineeDTO updateTrainee(String username, TraineeDTO traineeDTO){
        Trainee trainee = traineeRepository.searchTraineeByUsername(username);
    
        if (trainee == null) {
            return null; 
        }

        try {
            User updatedUser = userService.updateUser(username, traineeDTO.getUserDTO().getPassword(), traineeDTO.getUserDTO().getIsActive());
            if (updatedUser != null) {
                trainee.setUser(updatedUser);
            }
            if (traineeDTO.getAddress() != null && !traineeDTO.getAddress().trim().isEmpty()) {
                trainee.setAddress(traineeDTO.getAddress());
            }

        } catch (Exception e) {
            return null;
        }
        traineeRepository.save(trainee);

        Trainee updatedTrainee = traineeRepository.searchTraineeByUsername(username);
        User user = updatedTrainee.getUser();
        List<TraineeTrainersDTO> traineeTrainersDTOs = new ArrayList<>();
        if(updatedTrainee.getTraineeTrainers() != null){
            for (TraineeTrainers traineeTrainer : updatedTrainee.getTraineeTrainers()){
                TraineeTrainersDTO traineeTrainerDTO = traineeTrainersMapper.mapToDTO(traineeTrainer);
                traineeTrainersDTOs.add(traineeTrainerDTO);
            }
            return new TraineeDTO(new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), null, user.getIsActive()), updatedTrainee.getDateOfBirth(), updatedTrainee.getAddress(), traineeTrainersDTOs);
        }
        
        return new TraineeDTO(new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), null, user.getIsActive()), updatedTrainee.getDateOfBirth(), updatedTrainee.getAddress(), null);
       
    }

    public void deleteTrainee(String username, String password){
        try{
            if (userService.selectUser(username)!=null){
                traineeRepository.delete(traineeRepository.searchTraineeByUsername(username));
            }else{
                throw new Exception("Username or Password are not Valid");
            } 
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public TraineeDTO changePassword(TraineeDTO traineeDTO){
        Trainee trainee;
        try {
            User updatedUser = userService.changePassword(traineeDTO.getUserDTO().getUsername(), traineeDTO.getUserDTO().getPassword());
        
            if (updatedUser != null) {
                trainee = traineeRepository.searchTraineeByUsername(traineeDTO.getUserDTO().getUsername());
                if (trainee != null) {
                    trainee.setUser(updatedUser);
                    traineeRepository.save(trainee);
                    traineeDTO.getUserDTO().setPassword(updatedUser.getPassword());
                } else {
                    throw new Exception("Trainee not found for the given username");
                }
            } else {
                throw new Exception("Username or Password are not valid");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return traineeDTO; 
    }


    public TraineeDTO selectTrainee(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            Trainee trainee = traineeRepository.searchTraineeByUsername(username);
            User user = trainee.getUser();
            List<TraineeTrainersDTO> traineeTrainersDTOs = new ArrayList<>();
            if(trainee.getTraineeTrainers() != null){
                for (TraineeTrainers traineeTrainer : trainee.getTraineeTrainers()){
                    TraineeTrainersDTO traineeTrainerDTO = traineeTrainersMapper.mapToDTO(traineeTrainer);
                    traineeTrainersDTOs.add(traineeTrainerDTO);
                }
            }
            return new TraineeDTO(new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive()), trainee.getDateOfBirth(), trainee.getAddress(), traineeTrainersDTOs);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<TraineeTrainersDTO> updateTraineeTrainerList(String username, List<String> trainerUsernames){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            Trainee trainee = traineeRepository.searchTraineeByUsername(username);
            List<TraineeTrainers> traineeTrainers = trainee.getTraineeTrainers();
            List<TraineeTrainersDTO> traineeTrainersDTOs = new ArrayList<>();
            for (String trainerUsername : trainerUsernames){
                Trainer trainer = trainerRepository.searchTrainerByUsername(trainerUsername);
                if(trainer != null){
                    TraineeTrainers traineeTrainer = new TraineeTrainers();
                    traineeTrainer.setTrainee(trainee);
                    traineeTrainer.setTrainer(trainer);
                    traineeTrainers.add(traineeTrainer);
                    TraineeTrainersDTO traineeTraineDTO = traineeTrainersMapper.mapToDTO(traineeTrainer);
                    traineeTrainersDTOs.add(traineeTraineDTO);
                }
            }
            traineeRepository.save(trainee);
            return traineeTrainersDTOs;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<TrainerDTO> getNotAssignedTrainers(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            List<Trainer> trainers = traineeRepository.searchTrainersNotAssignedToTrainees(username);
            List<TrainerDTO> trainersDTOs = new ArrayList<>();
            for (Trainer trainer : trainers){
                trainersDTOs.add(trainerMapper.mapToDTO(trainer));
            }
            return trainersDTOs;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
