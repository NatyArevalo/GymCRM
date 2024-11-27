package com.gymcrm.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymcrm.Configurations.PasswordGenerator;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoUserImpl;

@Service
public class UserService {
    private final DaoUserImpl daoUser;

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    public UserService(DaoUserImpl daoUser) {
        this.daoUser = daoUser;
    }

    public User createUser(String firstname, String lastname, String isActive){
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

        user.setFirstName(firstname);
        user.setLastName(lastname);
        
        Integer timesUsernameExist = daoUser.validateUsername(firstname, lastname);
        if (timesUsernameExist > 0){
            user.setUsername(firstname + "." + lastname + timesUsernameExist);
        }else{
            user.setUsername(firstname + "." + lastname);
        }

        String generatedPassword = PasswordGenerator.generateRandomPassword();
        user.setPassword(generatedPassword);

        user.setIsActive(Boolean.valueOf(isActive));
        daoUser.create(user);

    } catch (Exception e) {
        logger.error("Failed to create User", e);
        System.out.println(e.getMessage());
        return null;
    }   
    
    logger.info("User created successfully: {}", user);
    return user;
}

    public User updateUser(String username, String password, String isActive){
        User user = daoUser.searchUserByUsername(username);
        
        if (user == null) {
            logger.error("User not found for username: {}", username);
            return null;  
        }

        try {

            if (!daoUser.passwordValidation(username, password)) {
                throw new Exception("Username or Password are not Valid");
            }

            if (isActive != null && !isActive.trim().isEmpty()) {
                user.setIsActive(Boolean.valueOf(isActive));
            }
            
        } catch (Exception e) {
            logger.error("Failed to update User, invalid credentials or error", e);
            System.out.println(e.getMessage());
            return null;
        }
        
        daoUser.update(user);
        logger.info("User updated successfully: {}", user);

        return user;
    }

    public User changePassword(String username, String oldPassword){
        User user = new User();
        try {
            if (daoUser.passwordValidation(username, oldPassword)) {
                user = daoUser.searchUserByUsername(username);
                
                if (user != null) {
                    String newPassword = PasswordGenerator.generateRandomPassword();
                    user.setPassword(newPassword);
                    daoUser.update(user);
                    logger.info("Password updated successfully for User: {}", user);
                } else {
                    throw new Exception("User not found for the given username");
                }
            } else {
                throw new Exception("Username or Password are not valid");
            }
        } catch (Exception e) {
            logger.error("Failed to change password, credentials not valid", e);
            System.out.println(e.getMessage());
            return null;
        }
        return user;
    }

    public User selectUser(String username){
        try{
            if (username == null || username.trim().isEmpty()){
                throw new Exception("Username cannot be empty or null");
            }
            return daoUser.searchUserByUsername(username);

        }catch (Exception e){
            logger.error("Failed to select User", e);
            System.out.println(e.getMessage());
            return null;
        }
    }


}

