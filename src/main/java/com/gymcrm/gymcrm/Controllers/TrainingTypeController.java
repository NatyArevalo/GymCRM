package com.gymcrm.gymcrm.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymcrm.gymcrm.Entities.TrainingType;
import com.gymcrm.gymcrm.Services.TrainerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/trainingType")
@Tag(name = "Training Type Management")
public class TrainingTypeController {

    @Autowired
    TrainerService trainerService;

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    @GetMapping("/list")
        @Operation(summary = "Retrieve list of all training types")
    @ApiResponses({
        @ApiResponse(responseCode  = "201", description = "Training types retrieved successfully"),
        @ApiResponse(responseCode  = "404", description = "Training types not found"),
    })
    public List<TrainingType> getTrainingTypeList() {
        String transactionId = MDC.get("transactionId");
        logger.info("Processing request. transactionId={}", transactionId);
        return trainerService.getTrainingTypeList();
    }
    
}
