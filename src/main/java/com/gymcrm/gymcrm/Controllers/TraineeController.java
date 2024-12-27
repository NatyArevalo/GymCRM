package com.gymcrm.gymcrm.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.TraineeTrainersDTO;
import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.Services.TraineeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/Trainees")
@Tag(name = "Trainee Management")
public class TraineeController {

    @Autowired
    TraineeService traineeService;
    


    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new trainee")
    @ApiResponses({
        @ApiResponse(responseCode  = "201", description = "Trainee created successfully"),
        @ApiResponse(responseCode  = "401", description = "Invalid input")
    })
    public TraineeDTO createTrainee(@Parameter(description = "Details of the trainee to be created", required = true) @RequestBody TraineeDTO traineeDTO){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return traineeService.createTrainee(traineeDTO);
    }

    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change trainee login credentials")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Login updated successfully"),
        @ApiResponse(responseCode  = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> changeLogin(@Parameter(description = "Details of the trainee for login update", required = true) @RequestBody TraineeDTO traineeDTO) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        TraineeDTO trainee = traineeService.changePassword(traineeDTO);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful for user: " + trainee.getUserDTO().getUsername());
    }


    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve trainee profile by username")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainee profile retrieved successfully"),
        @ApiResponse(responseCode  = "404", description = "Trainee not found")
    })
    public TraineeDTO getTraineeProfile(@Parameter(description = "Username of the trainee", required = true) @PathVariable String username) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return traineeService.selectTrainee(username);
    }
    
    @PutMapping("/modify/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainee profile")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainee profile updated successfully"),
        @ApiResponse(responseCode  = "400", description = "Invalid input")
    })
    public TraineeDTO updateTraineeProfile(@Parameter(description = "Username of the trainee", required = true)  @PathVariable String username, @Parameter(description = "Updated details of the trainee", required = true) @RequestBody TraineeDTO traineeDTO) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return traineeService.updateTrainee(username, traineeDTO);
    }
    
    @DeleteMapping("/delete/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete trainee profile by username")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainee deleted successfully"),
        @ApiResponse(responseCode  = "401", description = "Invalid username or password\"")
    })
    public ResponseEntity<String> deleteTraineeProfile(@Parameter(description = "Username of the trainee", required = true) @PathVariable String username, @Parameter(description = "Password for authentication", required = true) @RequestBody String password){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        traineeService.deleteTrainee(username, password);
        TraineeDTO trainee = traineeService.selectTrainee(username);
        if (trainee != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Deletion successful for user: " + username);
    }

    @PutMapping("/modify/trainer-list/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainee's trainer list", description = "This endpoint updates the list of trainers assigned to a trainee.")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainer list updated successfully"),
        @ApiResponse(responseCode  = "400", description = "Invalid input")
    })
    public List<TraineeTrainersDTO> updateTraineeTrainerList(@Parameter(description = "Username of the trainee", required = true) @PathVariable String username, @Parameter(description = "List of trainer usernames", required = true) @RequestBody List<String> trainerUsernames) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        
        return traineeService.updateTraineeTrainerList(username, trainerUsernames);
    }

    @GetMapping("/not-assigned-trainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get unassigned trainers", description = "This endpoint retrieves a list of trainers not assigned to a trainee.")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "List retrieved successfull"),
    })
    public List<TrainerDTO> getMethodName(@Parameter(description = "Username of the trainee", required = true) @PathVariable String username) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return traineeService.getNotAssignedTrainers(username);
    }
    

}
