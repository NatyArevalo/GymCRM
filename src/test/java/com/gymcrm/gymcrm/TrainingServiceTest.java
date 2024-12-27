package com.gymcrm.gymcrm;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainerTraineesDTO;
import com.gymcrm.gymcrm.DTO.TrainingDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.Trainee;
import com.gymcrm.gymcrm.Entities.Trainer;
import com.gymcrm.gymcrm.Entities.Training;
import com.gymcrm.gymcrm.Entities.TrainingType;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.TrainingRepository;
import com.gymcrm.gymcrm.Services.TrainingService;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;
 

    @ParameterizedTest
    @MethodSource("provideTraining")
    public void testCreateTraining(TrainerDTO trainerDTO, TraineeDTO traineeDTO, TrainingDTO trainingDTO) {
       

        Trainer mockTrainer = new Trainer(1L, new User(1L, trainerDTO.getUserDTO().getFirstName(), trainerDTO.getUserDTO().getLastName(), trainerDTO.getUserDTO().getUsername(), "password", true), new TrainingType(1L, trainerDTO.getSpecialization().getTrainingTypeName()),null);
        Trainee mockTrainee = new Trainee(1L, new User(1L, traineeDTO.getUserDTO().getFirstName(), traineeDTO.getUserDTO().getLastName(), traineeDTO.getUserDTO().getUsername(), "password", true), traineeDTO.getDateOfBirth(),traineeDTO.getAddress(), null);

        when(trainerRepository.searchTrainerByUsername(trainerDTO.getUserDTO().getUsername())).thenReturn(mockTrainer);
        when(traineeRepository.searchTraineeByUsername(traineeDTO.getUserDTO().getUsername())).thenReturn(mockTrainee);

        TrainingType mockTrainingType = new TrainingType(1L, trainingDTO.getTrainingTypeName().getTrainingTypeName());
        when(trainerRepository.searchTrainingTypeByName(trainingDTO.getTrainingTypeName().getTrainingTypeName())).thenReturn(mockTrainingType);


        Training createdTraining = trainingService.createTraining(trainingDTO);
        assertNotNull(createdTraining);
        assertEquals(trainingDTO.getTrainingName(), createdTraining.getTrainingName());
        assertEquals(trainingDTO.getTrainingTypeName().getTrainingTypeName(), createdTraining.getTrainingType().getTrainingTypeName());
        assertEquals(trainingDTO.getTrainingDate(), createdTraining.getTrainingDate());
        assertEquals(trainingDTO.getDuration(), createdTraining.getDuration());

    }

    static Stream<Arguments> provideTraining() {
    return Stream.of(
            Arguments.of(
                    new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", "password123", true), new TrainingTypeDTO("STRENGTH"), List.of(new TrainerTraineesDTO())),
                    new TraineeDTO(new UserDTO("Jane", "Doe", "Jane.DOe", "password123", true), LocalDate.of(2000, 12, 9), "Cartagena, Colombia", null),
                    new TrainingDTO("Leg day", new TrainingTypeDTO("STRENGTH"),LocalDate.of(2024, 10, 15), 1.5, "Jane.DOe","John.Doe")
            ));
    }
    
    
}
