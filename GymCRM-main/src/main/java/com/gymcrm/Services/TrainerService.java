package com.gymcrm.Services;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.TrainingType;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoTrainerImpl;



@Service
public class TrainerService {
    private final UserService userService;
    private final DaoTrainerImpl daoTrainer;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    public TrainerService(DaoTrainerImpl daoTrainer, UserService userService) {
        this.daoTrainer = daoTrainer;
        this.userService = userService;
    }

    public Trainer createTrainer(String firstname, String lastname, String isActive, String specialization){
        Trainer trainer = new Trainer();
        try{

            trainer.setUser(userService.createUser(firstname, lastname, isActive));

            if (specialization != null && !specialization.trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(daoTrainer.searchTrainingTypeByName(specialization));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    trainer.setSpecialization(trainingType);
                }    
            }else{
                throw new Exception("specialization cannot be empty");
            }

        } catch (Exception e) {
            logger.error("Failed to create Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }   
        daoTrainer.create(trainer); 
        logger.info("Trainer created successfully: {}", trainer.getId());
        return trainer;
    }

    public Trainer updateTrainer(String username, String password, String specialization, String isActive){
        Trainer trainer = daoTrainer.searchTrainerByUsername(username);
        if (trainer == null) {
            logger.error("Trainee not found for username: {}", username);
            return null; 
        }

        try {

            if(userService.updateUser(username, password, isActive)!=null){
                if (specialization != null && !specialization.trim().isEmpty()) {
                    Optional<TrainingType> response = Optional.ofNullable(daoTrainer.searchTrainingTypeByName(specialization));
                    if(response.isPresent()){
                        TrainingType trainingType = response.get();
                        trainer.setSpecialization(trainingType);
                    }    
                }
            }

        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainer.update(trainer);
        logger.info("Trainer updated successfully: {}", trainer.getId());
        return trainer;
    }

    public Trainer changePassword(String username, String oldPassword){
        Trainer trainer = new Trainer();
        try {
            User updatedUser = userService.changePassword(username, oldPassword);

            if(updatedUser != null){
                trainer = daoTrainer.searchTrainerByUsername(username);
                trainer.setUser(updatedUser);
                daoTrainer.update(trainer);
                logger.info("Password updated successfully for Trainee: {}", trainer.getId());
            }else {
                throw new Exception("Username or Password are not valid");
            }

        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        
        return trainer;

    }


    public Trainer selectTrainer(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            return daoTrainer.searchTrainerByUsername(username);

        }catch (Exception e){
            logger.error("Failed to select Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }
    }

    
}
