package com.gymcrm.gymcrm.Mappers;

import org.springframework.stereotype.Component;

import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {
    public UserDTO mapToDTO(User user){
        return new UserDTO(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive());
    }
}
