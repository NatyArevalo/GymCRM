package com.gymcrm.gymcrm.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "LogIn", description = "authentication")
public class PortalController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login User", description  = "Provide username and password to login.")
    @ApiResponses(value = {
        @ApiResponse(responseCode  = "200", description  = "Login successful"),
        @ApiResponse(responseCode  = "401", description  = "Invalid username or password")
    })
    public ResponseEntity<String> logIn(@Parameter(description = "Username of the user", required = true) @RequestParam String username,  @Parameter(description = "Password of the user", required = true) @RequestParam String password){
        UserDTO user = userService.validateUser(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful for user: " + user.getUsername());
    }
}
