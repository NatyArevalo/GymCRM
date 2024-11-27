package com.gymcrm;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.gymcrm.Entities.Trainee;
import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.Training;
import com.gymcrm.Entities.TrainingType;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoTraineeImpl;
import com.gymcrm.Persistence.DaoTrainerImpl;
import com.gymcrm.Persistence.DaoTrainingImpl;
import com.gymcrm.Persistence.DaoUserImpl;
import com.gymcrm.Services.TraineeService;
import com.gymcrm.Services.TrainerService;
import com.gymcrm.Services.TrainingService;
import com.gymcrm.Services.UserService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainingServiceTest {
    private UserService userService;
    private DaoUserImpl daoUser;
    private DaoTrainerImpl daoTrainer;
    private TrainerService trainerService;
    private DaoTraineeImpl daoTrainee;
    private TraineeService traineeService;
    private DaoTrainingImpl daoTraining;
    private TrainingService trainingService;

    @BeforeAll
    public void setUp(){
        daoUser = Mockito.mock(DaoUserImpl.class);
        daoTrainer = Mockito.mock(DaoTrainerImpl.class);
        daoTrainee = Mockito.mock(DaoTraineeImpl.class);
        userService = new UserService(daoUser);
        trainerService = new TrainerService(daoTrainer, userService);
        traineeService = new TraineeService(daoTrainee, userService);
        daoTraining = Mockito.mock(DaoTrainingImpl.class);
        trainingService = new TrainingService(daoTraining, daoTrainee, daoTrainer);
    }

    @ParameterizedTest
    @MethodSource("provideTraining")
    public void testCreateTraining(String traineeFirstname, String traineeLastname, String traineeUsername, String traineeIsActive, Integer traineeYearOfBirth, Integer traineeMonthOfBirth, Integer traineeDayOfBirth, String traineeAddress,
                                   String trainerFirstName, String trainerLastName, String trainerUsername, String trainerIsActive, String trainerSpecialization,
                                   String trainingName, String trainingSpecialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double durationOfTraining) {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0); 
            user.setId(1L); 
            return null; 
        }).when(daoUser).create(any(User.class));
        
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0); 
            trainee.setId(1L);
            return null; 
        }).when(daoTrainee).create(any(Trainee.class));

        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0); 
            trainer.setId(1L);
            return null; 
        }).when(daoTrainer).create(any(Trainer.class));

        doAnswer(invocation -> {
            Training training = invocation.getArgument(0); 
            training.setId(1L);
            return null; 
        }).when(daoTraining).create(any(Training.class));

        when(daoTrainer.searchTrainingTypeByName(trainingSpecialization))
        .thenReturn(new TrainingType(1L, trainingSpecialization));

        Trainee createdTrainee = traineeService.createTrainee(traineeFirstname, traineeLastname, traineeIsActive, traineeYearOfBirth, traineeMonthOfBirth, traineeDayOfBirth, traineeAddress);
        assertNotNull(createdTrainee);

        Trainer createdTrainer = trainerService.createTrainer(trainerFirstName, trainerLastName, trainerIsActive, trainerSpecialization);
        assertNotNull(createdTrainer);

        when(daoTrainer.getTrainerById(createdTrainer.getId()))
        .thenReturn(new Trainer(1L, new User(1L, trainerFirstName, trainerLastName, trainerUsername, "password123", Boolean.valueOf(trainerIsActive)),
        new TrainingType(1L, trainerSpecialization)));

        when(daoTrainee.getTraineeById(createdTrainee.getId()))
        .thenReturn(new Trainee(1L, new User(1L, traineeFirstname, traineeLastname, traineeUsername, "password123", Boolean.valueOf(traineeIsActive)),LocalDate.of(traineeYearOfBirth, traineeMonthOfBirth, traineeDayOfBirth), traineeAddress));

        Training createdTraining = trainingService.createTraining(createdTrainee.getId(), createdTrainer.getId(), trainingName, trainingSpecialization, yearOfTraning, monthOfTraining, dayOfTraining, durationOfTraining);
        assertNotNull(createdTraining);
        assertEquals(trainingName, createdTraining.getTrainingName());
        assertEquals(trainingSpecialization, createdTraining.getTrainingType().getTrainingTypeName());
        assertEquals(LocalDate.of(yearOfTraning, monthOfTraining, dayOfTraining), createdTraining.getTrainingDate());
        assertEquals(durationOfTraining, createdTraining.getDuration());

    }

    static Stream<Object[]> provideTraining() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                         "Jane", "Doe", "John.Doe", "true", "FUNCTIONAL",
                         "Leg day", "FUNCTIONAL", 2024, 10, 15, 1.5},
                new Object[]{"John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                         "Jane", "Doe", "John.Doe", "true", "FUNCTIONAL",
                         "Leg day", "FUNCTIONAL", 2024, 10, 15, 1.5}
        );
    }
    
    
}
