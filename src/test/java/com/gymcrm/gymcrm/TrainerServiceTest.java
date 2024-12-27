package com.gymcrm.gymcrm;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gymcrm.gymcrm.DTO.TrainerDTO;
import com.gymcrm.gymcrm.DTO.TrainerTraineesDTO;
import com.gymcrm.gymcrm.DTO.TrainingTypeDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.Trainer;
import com.gymcrm.gymcrm.Entities.TrainingType;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Services.TrainerService;
import com.gymcrm.gymcrm.Services.UserService;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;


    @ParameterizedTest
    @MethodSource("provideTrainers")
    public void testCreateTrainer(TrainerDTO trainerDTO) {
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(trainerDTO.getUserDTO().getFirstName());
        createdUser.setLastName(trainerDTO.getUserDTO().getLastName());
        createdUser.setUsername(trainerDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(trainerDTO.getUserDTO().getIsActive());

        when(userService.createUser(trainerDTO.getUserDTO())).thenReturn(createdUser);

        TrainerDTO createdTrainer = trainerService.createTrainer(trainerDTO);
        assertNotNull(createdTrainer);
        assertEquals(trainerDTO.getUserDTO().getUsername(), createdTrainer.getUserDTO().getUsername());
        assertNotNull(createdTrainer.getUserDTO().getPassword());
        assertNotEquals(trainerDTO.getUserDTO().getPassword(), createdTrainer.getUserDTO().getPassword());

    }
    static Stream<TrainerDTO> provideTrainers() {
        return Stream.of(
            new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", null, true), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null,false), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           new TrainingTypeDTO("STRENGTH"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("updateTrainers")
    public void testUpdateTrainer(TrainerDTO trainerDTO, TrainerDTO updatedTrainerDTO){
    
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(trainerDTO.getUserDTO().getFirstName());
        createdUser.setLastName(trainerDTO.getUserDTO().getLastName());
        createdUser.setUsername(trainerDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(trainerDTO.getUserDTO().getIsActive());

        when(userService.createUser(trainerDTO.getUserDTO())).thenReturn(createdUser);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setUser(createdUser);
        existingTrainer.setSpecialization(new TrainingType(1L, trainerDTO.getSpecialization().getTrainingTypeName()));

        when(trainerRepository.searchTrainerByUsername(anyString())).thenReturn(existingTrainer);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setFirstName(createdUser.getFirstName());
        updatedUser.setLastName(createdUser.getLastName());
        updatedUser.setUsername(createdUser.getUsername());
        updatedUser.setPassword(createdUser.getPassword());
        updatedUser.setIsActive(
            updatedTrainerDTO.getUserDTO().getIsActive() != null ? updatedTrainerDTO.getUserDTO().getIsActive() : createdUser.getIsActive()
        );

        when(userService.updateUser(
            eq(trainerDTO.getUserDTO().getUsername()),
            eq(trainerDTO.getUserDTO().getPassword()),
            eq(updatedTrainerDTO.getUserDTO().getIsActive())
        )).thenReturn(updatedUser);
    
        TrainerDTO createdTrainer = trainerService.createTrainer(trainerDTO);
        assertNotNull(createdTrainer);

        TrainerDTO updatedTrainer = trainerService.updateTrainer(trainerDTO.getUserDTO().getUsername(),  updatedTrainerDTO);
        assertNotNull(updatedTrainer);
        assertEquals(createdTrainer.getUserDTO().getUsername(), updatedTrainer.getUserDTO().getUsername());
    
        assertEquals(updatedTrainerDTO.getUserDTO().getIsActive().toString().isEmpty() ? trainerDTO.getUserDTO().getIsActive() : updatedTrainerDTO.getUserDTO().getIsActive(), updatedTrainer.getUserDTO().getIsActive());
    
        assertEquals(updatedTrainerDTO.getSpecialization() == null ? trainerDTO.getSpecialization().getTrainingTypeName() : updatedTrainerDTO.getSpecialization().getTrainingTypeName(), updatedTrainer.getSpecialization().getTrainingTypeName());
    
        assertEquals(trainerDTO.getUserDTO().getFirstName(), updatedTrainer.getUserDTO().getFirstName());
        assertEquals(trainerDTO.getUserDTO().getLastName(), updatedTrainer.getUserDTO().getLastName());
    }

    static  Stream<Arguments> updateTrainers(){
        return Stream.of(
            Arguments.of(
                new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", null, true), new TrainingTypeDTO("STRENGTH"), List.of(new TrainerTraineesDTO())),
                new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", null, false), null, List.of(new TrainerTraineesDTO()))
            ),
            Arguments.of(
                new TrainerDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null, false), new TrainingTypeDTO("STRENGTH"), null),
                new TrainerDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null, true), null, null)
            ),
            Arguments.of(
                new TrainerDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null, true), new TrainingTypeDTO("STRENGTH"), null),
                new TrainerDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null, false), null, null)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("selectTrainers")
    public void testSelectTrainer(TrainerDTO trainerDTO) {
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(trainerDTO.getUserDTO().getFirstName());
        createdUser.setLastName(trainerDTO.getUserDTO().getLastName());
        createdUser.setUsername(trainerDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(trainerDTO.getUserDTO().getIsActive());

        when(userService.createUser(trainerDTO.getUserDTO())).thenReturn(createdUser);
    
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setUser(createdUser);
        existingTrainer.setSpecialization(new TrainingType(1L, trainerDTO.getSpecialization().getTrainingTypeName()));

        when(trainerRepository.searchTrainerByUsername(anyString())).thenReturn(existingTrainer);
    
        TrainerDTO createdTrainer = trainerService.createTrainer(trainerDTO);
        assertNotNull(createdTrainer);

        TrainerDTO selectedTrainer = trainerService.selectTrainer(trainerDTO.getUserDTO().getUsername());

        if(trainerDTO.getUserDTO().getUsername() == null){
            assertNull(selectedTrainer);
        }else {
            assertNotNull(selectedTrainer);
            assertEquals(createdTrainer.getUserDTO().getUsername(), selectedTrainer.getUserDTO().getUsername());
            assertEquals(createdTrainer.getUserDTO().getPassword(), selectedTrainer.getUserDTO().getPassword());
        }

    }

    static  Stream<TrainerDTO> selectTrainers(){
        return Stream.of(
            new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", null, true), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null,false), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           new TrainingTypeDTO("STRENGTH"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("changedPassWord")
    public void testChangedPassword(TrainerDTO trainerDTO){
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(trainerDTO.getUserDTO().getFirstName());
        createdUser.setLastName(trainerDTO.getUserDTO().getLastName());
        createdUser.setUsername(trainerDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(trainerDTO.getUserDTO().getIsActive());

        when(userService.createUser(trainerDTO.getUserDTO())).thenReturn(createdUser);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setFirstName(createdUser.getFirstName());
        updatedUser.setLastName(createdUser.getLastName());
        updatedUser.setUsername(createdUser.getUsername());
        updatedUser.setPassword("updatedPas"); 
        updatedUser.setIsActive(createdUser.getIsActive());


        when(userService.changePassword(createdUser.getUsername(), "randomPassword")).thenReturn(updatedUser);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(1L);
        existingTrainer.setUser(createdUser);
        existingTrainer.setSpecialization(new TrainingType(1L, trainerDTO.getSpecialization().getTrainingTypeName()));

        when(trainerRepository.searchTrainerByUsername(anyString())).thenReturn(existingTrainer);

        TrainerDTO createdTrainer = trainerService.createTrainer(trainerDTO);
        assertNotNull(createdTrainer);
        String oldPassword = createdTrainer.getUserDTO().getPassword();

        TrainerDTO changedPasswordTrainer = trainerService.changePassword(trainerDTO);
        if(trainerDTO.getUserDTO().getPassword() == null){
            assertNull(changedPasswordTrainer);
        }else{
            assertNotNull(changedPasswordTrainer);

            assertNotEquals(oldPassword, changedPasswordTrainer.getUserDTO().getPassword());
            assertEquals("updatedPas", changedPasswordTrainer.getUserDTO().getPassword());
        }
     }
    static Stream<TrainerDTO> changedPassWord() {
        return Stream.of(
            new TrainerDTO(new UserDTO("John", "Doe", "John.Doe", "randomPassword", true), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Jane", "Smith", "Jane.Smith", "randomPassword",false), 
                           new TrainingTypeDTO("STRENGTH"), null),
            new TrainerDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           new TrainingTypeDTO("STRENGTH"), null)
        );
    }
}
