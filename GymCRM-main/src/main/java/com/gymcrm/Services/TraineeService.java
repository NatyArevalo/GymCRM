package com.gymcrm.Services;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Entities.Trainee;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoTraineeImpl;


@Service
public class TraineeService {
    private final UserService userService;
    private final DaoTraineeImpl daoTrainee;
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

     @Autowired
    public TraineeService(DaoTraineeImpl daoTrainee, UserService userService) {
        this.daoTrainee = daoTrainee;
        this.userService = userService;
    }

    public Trainee createTrainee(String firstname, String lastname, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address){
        Trainee trainee = new Trainee();
        try {

            trainee.setUser(userService.createUser(firstname, lastname, isActive));
            if (yearOfBirth == null ||  monthOfBirth == null || dayOfBirth == null) {
                throw new Exception("Birth fields cannot be empty");
            }

            trainee.setDateOfBirth(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth));
            trainee.setAddress(address);

        } catch (Exception e){
            logger.error("Failed to create Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }

        daoTrainee.create(trainee);
        logger.info("Trainee created successfully: {}", trainee.getId());
        return trainee;  
    }

    public Trainee updateTrainee(String username, String password, String isActive, String address){
         Trainee trainee = daoTrainee.searchTraineeByUsername(username);
    
        if (trainee == null) {
            logger.error("Trainee not found for username: {}", username);
            return null; 
        }

        try {
            User updatedUser = userService.updateUser(username, password, isActive);
            if (updatedUser != null) {
                trainee.setUser(updatedUser);
            }
            if (address != null && !address.trim().isEmpty()) {
                trainee.setAddress(address);
            }

        } catch (Exception e) {
            logger.error("Failed to update Trainee", e);
            return null;
        }
        daoTrainee.update(trainee);
        logger.info("Trainee updated successfully: {}", trainee.getId());

        return trainee;
    }

    public void deleteTrainee(String username, String password){
        try{
            if (userService.selectUser(username)!=null){
                daoTrainee.delete(selectTrainee(username));
                logger.info("Trainee deleted successfully: {}", username);
            }else{
                throw new Exception("Username or Password are not Valid");
            }
            
        }catch (Exception e){
            logger.error("Failed to delete Trainee", e);
            System.out.println(e.getMessage());
        }
    }

    public Trainee changePassword(String username, String oldPassword){
        Trainee trainee = new Trainee();
        try {
            User updatedUser = userService.changePassword(username, oldPassword);
        
            if (updatedUser != null) {
                trainee = daoTrainee.searchTraineeByUsername(username);
                if (trainee != null) {
                    trainee.setUser(updatedUser);
                    daoTrainee.update(trainee);
                    logger.info("Password updated successfully for Trainee: {}", trainee.getId());
                } else {
                    throw new Exception("Trainee not found for the given username");
                }
            } else {
                throw new Exception("Username or Password are not valid");
            }
        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        return trainee; 

    }


    public Trainee selectTrainee(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }

            return daoTrainee.searchTraineeByUsername(username);

        }catch (Exception e){
            logger.error("Failed to select Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }
    }
}
