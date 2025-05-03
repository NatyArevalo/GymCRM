package com.gymcrm.Services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Configurations.PasswordGenerator;
import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.User;
import com.gymcrm.Entities.TrainingType;
import com.gymcrm.Persistence.DaoTrainerImpl;
import com.gymcrm.Persistence.DaoUserImpl;

@Service
public class TrainerService {
    
    private final DaoUserImpl daoUser;
    private final DaoTrainerImpl daoTrainer;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    public TrainerService(DaoTrainerImpl daoTrainer, DaoUserImpl daoUser) {
        this.daoTrainer = daoTrainer;
        this.daoUser = daoUser;
    }

    public Trainer createTrainer(String firstname, String lastname, String isActive, String specialization){
        Trainer trainer = new Trainer();
        User user = new User();
        try{

            if(firstname == null || firstname.trim().isEmpty()){
                throw new Exception("first name cannot be empty");
            }
            if(lastname == null || lastname.trim().isEmpty()){
                throw new Exception("last name cannot be empty");
            }
            
            if(isActive == null || isActive.trim().isEmpty()){
                user.setIsActive(Boolean.FALSE);
            }

            if (specialization != null && !specialization.trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(daoTrainer.searchTrainingTypeByName(specialization));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    trainer.setSpecialization(trainingType);
                }    
            }else{
                throw new Exception("specialization cannot be empty");
            }

            user.setFirstName(firstname);
            user.setLastName(lastname);
            
            Integer timesUsernameExist = daoTrainer.validateUsername(firstname, lastname);
            if (timesUsernameExist > 0){
                user.setUsername(firstname + "." + lastname + timesUsernameExist);
            }else{
                user.setUsername(firstname + "." + lastname);
            }

            String generatedPassword = PasswordGenerator.generateRandomPassword();
            user.setPassword(generatedPassword);

            user.setIsActive(Boolean.valueOf(isActive));
            daoUser.create(user);
            trainer.setUser(user);

        } catch (Exception e) {
            logger.error("Failed to create Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }   
        daoTrainer.create(trainer); 
        logger.info("Trainer created successfully: {}", trainer);
        return trainer;
    }

    public Trainer updateTrainer(String username, String password, String specialization, String isActive){
        Trainer trainer = new Trainer();
        try {
            if(daoTrainer.passwordValidation(username, password)){
                trainer = daoTrainer.searchTrainerByUsername(username);
            }else{
                throw new Exception("Username or Password are not Valid");
            }

            if (isActive != null && !isActive.trim().isEmpty()) {
                trainer.setIsActive(Boolean.valueOf(isActive));
            }
            if (specialization != null && !specialization.trim().isEmpty()) {
                Optional<TrainingType> response = Optional.ofNullable(daoTrainer.searchTrainingTypeByName(specialization));
                if(response.isPresent()){
                    TrainingType trainingType = response.get();
                    trainer.setSpecialization(trainingType);
                }    
            }

        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainer.update(trainer);
        logger.info("Trainer updated successfully: {}", trainer);
        return trainer;
    }

    public Trainer changePassword(String username, String oldPassword){
        Trainer trainer = new Trainer();
        try {
            if(daoTrainer.passwordValidation(username, oldPassword)){
                trainer = daoTrainer.searchTrainerByUsername(username);
            }else{
                throw new Exception("Username or Password are not Valid");
            }

            String newPassword = PasswordGenerator.generateRandomPassword();
            trainer.setPassword(newPassword);

        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainer.update(trainer);
        logger.info("Password updated succesfully: {}", trainer);
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
