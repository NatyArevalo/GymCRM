package com.gymcrm.gymcrm.Controllers;

import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.AuthenticationResponse;
import com.gymcrm.gymcrm.Services.AuthenticationService;
import com.gymcrm.gymcrm.Services.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

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
    AuthenticationService authenticationService;
    @Autowired
    LoginAttemptService loginAttemptService;


    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login User", description  = "Provide username and password to login.")
    @ApiResponses(value = {
        @ApiResponse(responseCode  = "200", description  = "Login successful"),
        @ApiResponse(responseCode  = "401", description  = "Invalid username or password")
    })
    public ResponseEntity<String> logIn(@Parameter(description = "User that cointains Username and password only", required = true) @RequestBody UserDTO userDTO){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        try {
            if (userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
                return ResponseEntity.badRequest().body("Username must not be null or empty.");
            }
            if (loginAttemptService.isBlocked(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Account is locked. Try again later.");
            }

            AuthenticationResponse token = authenticationService.authenticate(userDTO);
            loginAttemptService.loginSucceeded(userDTO.getUsername());

           return ResponseEntity.ok(token.getAccessToken());
       }catch (BadCredentialsException e) {
           loginAttemptService.loginFailed(userDTO.getUsername());
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
       }
    }



//    @PostMapping("/logout")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        String transactionId = MDC.get("transactionId");
//        logger.info("Processing request. transactionId={}", transactionId);
//        SecurityContextHolder.clearContext();
//        request.getSession().invalidate();
//        return ResponseEntity.ok("Logged out successfully");
//    }
}
