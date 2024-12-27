package com.gymcrm.gymcrm.DTO;

import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "User Info")
public class UserDTO {
    @NotNull
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}
