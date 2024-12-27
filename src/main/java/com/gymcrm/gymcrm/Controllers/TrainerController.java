package com.gymcrm.gymcrm.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.Services.TrainerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/Trainers")
@Tag(name = "Trainer Management")
public class TrainerController {
    
    @Autowired
    TrainerService trainerService;

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new trainer")
    @ApiResponses({
        @ApiResponse(responseCode  = "201", description = "Trainer created successfully"),
        @ApiResponse(responseCode  = "401", description = "Invalid input")
    })
    public TrainerDTO createTrainer(@Parameter(description = "Details of the trainer to be created", required = true) @RequestBody TrainerDTO trainerDTO){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainerService.createTrainer(trainerDTO);
    }

    
    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change trainer login credentials")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Login updated successfully"),
        @ApiResponse(responseCode  = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> changePassword(@Parameter(description = "Details of the trainer for login update", required = true) @RequestBody TrainerDTO trainerDTO) {
        TrainerDTO trainer = trainerService.changePassword(trainerDTO);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful for user: " + trainer.getUserDTO().getUsername());
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve trainer profile by username")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainer profile retrieved successfully"),
        @ApiResponse(responseCode  = "404", description = "Trainer not found")
    })
    public TrainerDTO getTrainerProfile(@Parameter(description = "Username of the trainer", required = true) @PathVariable String username) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainerService.selectTrainer(username);
    }

    @PutMapping("/modify/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainer profile")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainer profile updated successfully"),
        @ApiResponse(responseCode  = "400", description = "Invalid input")
    })
    public TrainerDTO updateTrainerProfile (@Parameter(description = "Username of the trainer", required = true) @PathVariable String username, @Parameter(description = "Updated details of the trainer", required = true) @RequestBody TrainerDTO trainerDTO) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainerService.updateTrainer(username, trainerDTO);
    }
    
    @PatchMapping("/modify/status/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainer active status", description = "This endpoint updates the active status of a trainer by username.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active status updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> updateActiveStatus(@Parameter(description = "Username of the trainer", required = true) @PathVariable String username, @Parameter(description = "Updated active status of the trainer", required = true) @RequestBody TrainerDTO trainerDTO){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        TrainerDTO trainer = trainerService.updateTrainer(username, trainerDTO);
        if(trainer == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Active status successfully updated for user: " + trainer.getUserDTO().getUsername());
    }

}
