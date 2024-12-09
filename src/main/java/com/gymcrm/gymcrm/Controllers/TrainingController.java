package com.gymcrm.gymcrm.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gymcrm.gymcrm.DTO.TrainingDTO;
import com.gymcrm.gymcrm.Entities.Training;
import com.gymcrm.gymcrm.Services.TrainingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/Trainings")
@Tag(name = "Trainings Management")
public class TrainingController {
    @Autowired
    TrainingService trainingService;

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training")
    @ApiResponses({
        @ApiResponse(responseCode  = "201", description = "Training created successfully"),
        @ApiResponse(responseCode  = "400", description = "Invalid input"),
        @ApiResponse(responseCode  = "400", description = "Unauthorized access")
    })
    public ResponseEntity<String> createTraining(@Parameter(description = "Details of the training to be created", required = true) @RequestBody TrainingDTO trainingDTO){
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        Training training = trainingService.createTraining(trainingDTO);
        if (training == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Data Provided");
        }
        return ResponseEntity.ok("Training created successfully: " + training.getTrainingName());
    }

    @GetMapping("/trainee/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve trainings for a trainee")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainings retrieved successfully"),
        @ApiResponse(responseCode  = "404", description = "Trainings not found")
    })
    public List<TrainingDTO> getTraineeTrainings(@Parameter(description = "Username of the trainee", required = true) @PathVariable String username, 
                                                @Parameter(description = "Start date for the trainings filter (optional)", required = false) @RequestParam LocalDate dateFrom, 
                                                @Parameter(description = "End date for the trainings filter (optional)", required = false) @RequestParam LocalDate dateTo, 
                                                @Parameter(description = "Username of the trainer (optional)", required = false) @RequestParam  String trainerUsername, 
                                                @Parameter(description = "Type of training (optional)", required = false) @RequestParam String trainingType) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainingService.getTraineeTrainings(username, dateFrom, dateTo, trainerUsername, trainingType);
    }

    @GetMapping("/trainer/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve trainings for a trainer")
    @ApiResponses({
        @ApiResponse(responseCode  = "200", description = "Trainings retrieved successfully"),
        @ApiResponse(responseCode  = "404", description = "Trainings not found")
    })
    public List<TrainingDTO> getTrainerTrainings(@Parameter(description = "Username of the trainer", required = true) @PathVariable String username, 
                                                @Parameter(description = "Start date for the trainings filter (optional)", required = false) @RequestParam LocalDate dateFrom, 
                                                @Parameter(description = "End date for the trainings filter (optional)", required = false) @RequestParam LocalDate dateTo, 
                                                @Parameter(description = "Username of the trainee (optional)", required = false) @RequestParam String traineeUsername) {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainingService.getTrainerTrainings(username, dateFrom, dateTo, traineeUsername);
    }
    

}
