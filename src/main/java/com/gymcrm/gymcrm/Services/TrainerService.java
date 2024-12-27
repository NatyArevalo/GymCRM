package com.gymcrm.gymcrm.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gymcrm.gymcrm.Enumerators.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainerTraineesDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.TraineeTrainers;
import com.gymcrm.gymcrm.Entities.Trainer;
import com.gymcrm.gymcrm.Entities.TrainingType;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Mappers.TrainerTraineesMapper;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;


@Service
public class TrainerService {
    @Autowired
    UserService userService;
    
    @Autowired
    TrainerRepository trainerRepository;
    
    @Autowired
    TrainerTraineesMapper trainerTraineesMapper;


    public TrainerDTO createTrainer(TrainerDTO trainerDTO){
        Trainer trainer = new Trainer();
        try{

            trainer.setUser(userService.createUser(trainerDTO.getUserDTO()));
            trainer.getUser().setRol(Rol.TRAINER);
            if (trainerDTO.getSpecialization() != null && !trainerDTO.getSpecialization().getTrainingTypeName().trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(trainerRepository.searchTrainingTypeByName(trainerDTO.getSpecialization().getTrainingTypeName()));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    trainer.setSpecialization(trainingType);
                }    
            }else{
                throw new Exception("specialization cannot be empty");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }   
        trainerRepository.save(trainer); 
        return new TrainerDTO(new UserDTO(null, null, trainer.getUser().getUsername(), trainer.getUser().getPassword(),null),null,null);  
    }

    public TrainerDTO updateTrainer(String username, TrainerDTO trainerDTO){
        Trainer trainer = trainerRepository.searchTrainerByUsername(username);
        if (trainer == null) {
            return null; 
        }

        try {
            User updatedUser = userService.updateUser(username, trainerDTO.getUserDTO().getPassword(), trainerDTO.getUserDTO().getIsActive());
            if(updatedUser !=null){
                trainer.setUser(updatedUser);
                if (trainerDTO.getSpecialization() != null && !trainerDTO.getSpecialization().getTrainingTypeName().trim().isEmpty()) {
                    Optional<TrainingType> response = Optional.ofNullable(trainerRepository.searchTrainingTypeByName(trainerDTO.getSpecialization().getTrainingTypeName()));
                    if(response.isPresent()){
                        TrainingType trainingType = response.get();
                        trainer.setSpecialization(trainingType);
                    }    
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        trainerRepository.save(trainer);
        Trainer updatedTrainer = trainerRepository.searchTrainerByUsername(username);
        User user = updatedTrainer.getUser();
        List<TrainerTraineesDTO> trainerTraineesDTOs = new ArrayList<>();
        if(updatedTrainer.getTrainerTrainees() != null){
            for (TraineeTrainers trainerTrainee : updatedTrainer.getTrainerTrainees()){
                TrainerTraineesDTO trainerTraineesDTO = trainerTraineesMapper.mapToDTO(trainerTrainee);
                trainerTraineesDTOs.add(trainerTraineesDTO);
            }
        }
        return new TrainerDTO(new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), null, user.getIsActive()), new TrainingTypeDTO(updatedTrainer.getSpecialization().getTrainingTypeName()), trainerTraineesDTOs);
    }

    public TrainerDTO changePassword(TrainerDTO trainerDTO){
        Trainer trainer = new Trainer();
        try {
            User updatedUser = userService.changePassword(trainerDTO.getUserDTO().getUsername(), trainerDTO.getUserDTO().getPassword());

            if(updatedUser != null){
                trainer = trainerRepository.searchTrainerByUsername(trainerDTO.getUserDTO().getUsername());
                trainer.setUser(updatedUser);
                trainerRepository.save(trainer);
                trainerDTO.getUserDTO().setPassword(updatedUser.getPassword());
            }else {
                throw new Exception("Username or Password are not valid");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return trainerDTO;
    }


    public TrainerDTO selectTrainer(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            Trainer trainer = trainerRepository.searchTrainerByUsername(username);
            User user = trainer.getUser();
            List<TrainerTraineesDTO> trainerTraineesDTOs = new ArrayList<>();
            if(trainer.getTrainerTrainees() != null){
                for (TraineeTrainers trainerTrainee : trainer.getTrainerTrainees()){
                    TrainerTraineesDTO trainerTraineesDTO = trainerTraineesMapper.mapToDTO(trainerTrainee);
                    trainerTraineesDTOs.add(trainerTraineesDTO);
                }
            }
             return new TrainerDTO(new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive()), new TrainingTypeDTO(trainer.getSpecialization().getTrainingTypeName()), trainerTraineesDTOs);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<TrainingType> getTrainingTypeList(){
        return trainerRepository.getTrainingTypes();
    }

    
}
