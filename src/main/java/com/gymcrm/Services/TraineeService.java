package com.gymcrm.Services;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Configurations.PasswordGenerator;
import com.gymcrm.Entities.Trainee;
import com.gymcrm.Persistence.DaoTraineeImpl;

@Service
public class TraineeService {

    private final DaoTraineeImpl daoTrainee;
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

     @Autowired
    public TraineeService(DaoTraineeImpl daoTrainee) {
        this.daoTrainee = daoTrainee;
    }

    public Trainee createTrainee(String firstname, String lastname, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address){
        Trainee trainee = new Trainee();
        try {
            if (firstname == null || firstname.trim().isEmpty()) {
                throw new Exception("first name cannot be empty");
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                throw new Exception("last name cannot be empty");
            }
            if (isActive == null || isActive.trim().isEmpty()) {
                trainee.setIsActive(Boolean.FALSE);
            }

            if (yearOfBirth == null ||  monthOfBirth == null || dayOfBirth == null) {
                throw new Exception("Birth fields cannot be empty");
            }

            trainee.setFirstName(firstname);
            trainee.setLastName(lastname);

            Integer timesUsernameExist = daoTrainee.validateUsername(firstname, lastname);
            if (timesUsernameExist > 0){
                trainee.setUsername(firstname + "." + lastname + timesUsernameExist);
            }else{
                trainee.setUsername(firstname + "." + lastname);
            }

            String generatedPassword = PasswordGenerator.generateRandomPassword();
            trainee.setPassword(generatedPassword);

            trainee.setIsActive(Boolean.valueOf(isActive));
            trainee.setDateOfBirth(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth));
            trainee.setAddress(address);

        } catch (Exception e){
            logger.error("Failed to create Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }

        daoTrainee.create(trainee);
        logger.info("Trainee created successfully: {}", trainee);
        return trainee;  
    }

    public Trainee updateTrainee(String username, String password, String isActive, String address){
        Trainee trainee = new Trainee();
        try{
            if(daoTrainee.passwordValidation(username, password)){
                trainee = daoTrainee.searchTraineeByUsername(username);
            }else{
                throw new Exception("Username or Password are not Valid");
            }

            if(isActive != null && !isActive.trim().isEmpty()){
                trainee.setIsActive(Boolean.valueOf(isActive));
            }
            if(address != null && !address.trim().isEmpty()){
                trainee.setAddress(address);
            }
    
        }catch (Exception e){
            logger.error("Failed to update Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainee.update(trainee);
        logger.info("Trainee updated successfully: {}", trainee);
        return trainee;
    }

    public void deleteTrainee(String username, String password){
        try{
            if (daoTrainee.passwordValidation(username, password)){
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
            if(daoTrainee.passwordValidation(username, oldPassword)){
                trainee = daoTrainee.searchTraineeByUsername(username);
            }else{
                throw new Exception("Username or Password are not Valid");
            }

            String newPassword = PasswordGenerator.generateRandomPassword();
            trainee.setPassword(newPassword);

        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainee.update(trainee);
        logger.info("Password updated succesfully: {}", trainee);
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
