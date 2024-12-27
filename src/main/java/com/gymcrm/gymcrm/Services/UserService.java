package com.gymcrm.gymcrm.Services;

import com.gymcrm.gymcrm.Entities.Token;
import com.gymcrm.gymcrm.Repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gymcrm.gymcrm.Configurations.PasswordGenerator;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.UserRepository;



@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    JwtService jwtService;


    public User createUser(UserDTO userDTO){
        User user = new User();
        try{

            if(userDTO.getFirstName() == null || userDTO.getFirstName().trim().isEmpty()){
                throw new Exception("first name cannot be empty");
            }
            if(userDTO.getLastName() == null || userDTO.getLastName().trim().isEmpty()){
                throw new Exception("last name cannot be empty");
            }
            
            if(userDTO.getIsActive() == null || userDTO.getIsActive().toString().trim().isEmpty()){
                user.setIsActive(Boolean.FALSE);
            }

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            
            Integer timesUsernameExist = userRepository.validateUsername(userDTO.getFirstName(), userDTO.getLastName()).intValue();
            if (timesUsernameExist > 0){
                user.setUsername(userDTO.getFirstName() + "." + userDTO.getLastName() + timesUsernameExist);
            }else{
                user.setUsername(userDTO.getFirstName() + "." + userDTO.getLastName());
            }

            String generatedPassword = PasswordGenerator.generateRandomPassword();
            System.out.println(generatedPassword);
            String password = passwordEncoder.encode(generatedPassword);
            user.setPassword(password);

            user.setIsActive(userDTO.getIsActive());
            userRepository.save(user);
            String token =  jwtService.generateToken(user);
            saveUserToken(token, user);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }   
        return user;
    }

    private void saveUserToken(String accessToken, User user) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setIsLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public User updateUser(String username, String password, Boolean isActive){
        User user = userRepository.searchUserByUsername(username);
        
        if (user == null) {
            return null;  
        }

        try {

            if (userRepository.passwordValidation(username, password).getIsActive() == null) {
                throw new Exception("Username or Password are not Valid");
            }

            if (isActive != null && !isActive.toString().trim().isEmpty()) {
                user.setIsActive(isActive);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        userRepository.save(user);
        return user;
    }

    public UserDTO validateUser(String username, String password){
        User user = selectUser(username);
        if(user!=null){
            user = userRepository.passwordValidation(username, password);
            if(user.getIsActive()){
                return new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(),user.getPassword(), user.getIsActive());
            }
        }else{
            return null;
        }
        return null;
    }
    

    public User changePassword(String username, String oldPassword){
        User user = new User();
        try {
            if (userRepository.passwordValidation(username, oldPassword).getIsActive()) {
                user = userRepository.searchUserByUsername(username);
                
                if (user != null) {
                    String newPassword = PasswordGenerator.generateRandomPassword();
                    user.setPassword(newPassword);
                    userRepository.save(user);
                } else {
                    throw new Exception("User not found for the given username");
                }
            } else {
                throw new Exception("Username or Password are not valid");
            }
        } catch (Exception e) {
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
            return userRepository.searchUserByUsername(username);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


}
