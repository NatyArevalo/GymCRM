package com.gymcrm.gymcrm;

import java.time.LocalDate;
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

import com.gymcrm.gymcrm.DTO.TraineeDTO;
import com.gymcrm.gymcrm.DTO.TraineeTrainersDTO;
import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.Trainee;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.TraineeRepository;
import com.gymcrm.gymcrm.Services.TraineeService;
import com.gymcrm.gymcrm.Services.UserService;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    @ParameterizedTest
    @MethodSource("provideTrainees")
    public void testCreateTrainee(TraineeDTO traineeDTO) {
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(traineeDTO.getUserDTO().getFirstName());
        createdUser.setLastName(traineeDTO.getUserDTO().getLastName());
        createdUser.setUsername(traineeDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(traineeDTO.getUserDTO().getIsActive());

        when(userService.createUser(traineeDTO.getUserDTO())).thenReturn(createdUser);
        
        TraineeDTO createdTrainee = traineeService.createTrainee(traineeDTO);
        assertNotNull(createdTrainee);
        assertEquals(traineeDTO.getUserDTO().getUsername(), createdTrainee.getUserDTO().getUsername());
        assertNotNull(createdTrainee.getUserDTO().getPassword());
        assertNotEquals(traineeDTO.getUserDTO().getPassword(), createdTrainee.getUserDTO().getPassword());
    }
    static Stream<TraineeDTO> provideTrainees() {
        return Stream.of(
            new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", null, true), 
                           LocalDate.of(2000, 12, 9), "Cartagena, Colombia", null),
            new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null,false), 
                           LocalDate.of(1995, 7, 30), "Bogotá, Colombia", null),
            new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           LocalDate.of(1988, 5, 20), "Medellín, Colombia",null)
        );
    }
    @ParameterizedTest
    @MethodSource("updateTrainees")
    public void testUpdateTrainee(TraineeDTO traineeDTO, TraineeDTO traineeDTOUpdated){

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(traineeDTO.getUserDTO().getFirstName());
        createdUser.setLastName(traineeDTO.getUserDTO().getLastName());
        createdUser.setUsername(traineeDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(traineeDTO.getUserDTO().getIsActive());

        when(userService.createUser(traineeDTO.getUserDTO())).thenReturn(createdUser);
    
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);
        existingTrainee.setUser(createdUser);
        existingTrainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        existingTrainee.setAddress(traineeDTO.getAddress());

        when(traineeRepository.searchTraineeByUsername(anyString())).thenReturn(existingTrainee);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setFirstName(createdUser.getFirstName());
        updatedUser.setLastName(createdUser.getLastName());
        updatedUser.setUsername(createdUser.getUsername());
        updatedUser.setPassword(createdUser.getPassword());
        updatedUser.setIsActive(
            traineeDTOUpdated.getUserDTO().getIsActive() != null ? traineeDTOUpdated.getUserDTO().getIsActive() : createdUser.getIsActive()
        );

        when(userService.updateUser(
            eq(traineeDTO.getUserDTO().getUsername()),
            eq(traineeDTO.getUserDTO().getPassword()),
            eq(traineeDTOUpdated.getUserDTO().getIsActive())
        )).thenReturn(updatedUser);
    
        TraineeDTO createdTrainee = traineeService.createTrainee(traineeDTO);
        assertNotNull(createdTrainee);
        
        TraineeDTO updatedTrainee = traineeService.updateTrainee(traineeDTO.getUserDTO().getUsername(),  traineeDTOUpdated);
        assertNotNull(updatedTrainee);
        assertEquals(createdTrainee.getUserDTO().getUsername(), updatedTrainee.getUserDTO().getUsername());
    
        assertEquals(traineeDTOUpdated.getUserDTO().getIsActive().toString().isEmpty() ? traineeDTO.getUserDTO().getIsActive() : traineeDTOUpdated.getUserDTO().getIsActive(), updatedTrainee.getUserDTO().getIsActive());
        String expectedAddress = (traineeDTOUpdated.getAddress() == null || traineeDTOUpdated.getAddress().isEmpty()) 
        ? traineeDTO.getAddress() 
        : traineeDTOUpdated.getAddress();

        assertEquals(expectedAddress, updatedTrainee.getAddress());
    
        assertEquals(traineeDTO.getUserDTO().getFirstName(), updatedTrainee.getUserDTO().getFirstName());
        assertEquals(traineeDTO.getUserDTO().getLastName(), updatedTrainee.getUserDTO().getLastName());
            
    }
    static Stream<Arguments> updateTrainees() {
        return Stream.of(
            Arguments.of(
                new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", null, true), LocalDate.of(2000, 12, 9), "Cartagena, Colombia", List.of(new TraineeTrainersDTO())),
                new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", null, false), LocalDate.of(2000, 12, 9), "Updated address", List.of(new TraineeTrainersDTO()))
            ),
            Arguments.of(
                new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null, false), LocalDate.of(1995, 7, 30), "Bogotá, Colombia", null),
                new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", null, true), LocalDate.of(1995, 7, 30), null, null)
            ),
            Arguments.of(
                new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null, true), LocalDate.of(1988, 5, 20), "Medellín, Colombia", null),
                new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null, false), LocalDate.of(1988, 5, 20), "Updated address", null)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("deleteTrainees")
    public void testDeleteTrainee(TraineeDTO traineeDTO){
       
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(traineeDTO.getUserDTO().getFirstName());
        createdUser.setLastName(traineeDTO.getUserDTO().getLastName());
        createdUser.setUsername(traineeDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(traineeDTO.getUserDTO().getIsActive());
    
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);
        existingTrainee.setUser(createdUser);
        existingTrainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        existingTrainee.setAddress(traineeDTO.getAddress());

        when(traineeRepository.searchTraineeByUsername(anyString())).thenReturn(existingTrainee);
                            

        traineeService.deleteTrainee(traineeDTO.getUserDTO().getUsername(), traineeDTO.getUserDTO().getPassword());
        assertNull(traineeService.selectTrainee(traineeDTO.getUserDTO().getUsername()));

    }
    static Stream<TraineeDTO> deleteTrainees() {
        return Stream.of(
            new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", "randomPassword", true), 
                           LocalDate.of(2000, 12, 9), "Cartagena, Colombia", null),
            new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", "randomPassword",false), 
                           LocalDate.of(1995, 7, 30), "Bogotá, Colombia", null),
            new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           LocalDate.of(1988, 5, 20), "Medellín, Colombia",null)
        );
    }

    @ParameterizedTest
    @MethodSource("selectTrainees")
    public void testSelectTrainee(TraineeDTO traineeDTO){
    
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(traineeDTO.getUserDTO().getFirstName());
        createdUser.setLastName(traineeDTO.getUserDTO().getLastName());
        createdUser.setUsername(traineeDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword");
        createdUser.setIsActive(traineeDTO.getUserDTO().getIsActive());

        when(userService.createUser(traineeDTO.getUserDTO())).thenReturn(createdUser);
    
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);
        existingTrainee.setUser(createdUser);
        existingTrainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        existingTrainee.setAddress(traineeDTO.getAddress());

        when(traineeRepository.searchTraineeByUsername(anyString())).thenReturn(existingTrainee);
    
        TraineeDTO createdTrainee = traineeService.createTrainee(traineeDTO);
        assertNotNull(createdTrainee);

        TraineeDTO selectedTrainee = traineeService.selectTrainee(traineeDTO.getUserDTO().getUsername());

        if(traineeDTO.getUserDTO().getUsername() == null){
            assertNull(selectedTrainee);
        }else {
            assertNotNull(selectedTrainee);
            assertEquals(createdTrainee.getUserDTO().getUsername(), selectedTrainee.getUserDTO().getUsername());
            assertEquals(createdTrainee.getUserDTO().getPassword(), selectedTrainee.getUserDTO().getPassword());
        }
    }
    static Stream<TraineeDTO> selectTrainees() {
        return Stream.of(
            new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", "randomPassword", true), 
                           LocalDate.of(2000, 12, 9), "Cartagena, Colombia", null),
            new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", "randomPassword",false), 
                           LocalDate.of(1995, 7, 30), "Bogotá, Colombia", null),
            new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           LocalDate.of(1988, 5, 20), "Medellín, Colombia",null)
        );
    }

    @ParameterizedTest
    @MethodSource("changedPassWord")
    public void testChangedPassword(TraineeDTO traineeDTO){
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setFirstName(traineeDTO.getUserDTO().getFirstName());
        createdUser.setLastName(traineeDTO.getUserDTO().getLastName());
        createdUser.setUsername(traineeDTO.getUserDTO().getUsername());
        createdUser.setPassword("randomPassword"); // Initial password
        createdUser.setIsActive(traineeDTO.getUserDTO().getIsActive());

        when(userService.createUser(traineeDTO.getUserDTO())).thenReturn(createdUser);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setFirstName(createdUser.getFirstName());
        updatedUser.setLastName(createdUser.getLastName());
        updatedUser.setUsername(createdUser.getUsername());
        updatedUser.setPassword("updatedPas"); 
        updatedUser.setIsActive(createdUser.getIsActive());


        when(userService.changePassword(createdUser.getUsername(), "randomPassword")).thenReturn(updatedUser);
        

        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);
        existingTrainee.setUser(updatedUser);
        existingTrainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        existingTrainee.setAddress(traineeDTO.getAddress());


        when(traineeRepository.searchTraineeByUsername(anyString())).thenReturn(existingTrainee);


        TraineeDTO createdTrainee = traineeService.createTrainee(traineeDTO);
        assertNotNull(createdTrainee);

        String oldPassword = traineeDTO.getUserDTO().getPassword();


        TraineeDTO changedPasswordTrainee = traineeService.changePassword(traineeDTO);
        if(traineeDTO.getUserDTO().getPassword() == null){
            assertNull(changedPasswordTrainee);
        }else{
            assertNotNull(changedPasswordTrainee);

            assertNotEquals(oldPassword, changedPasswordTrainee.getUserDTO().getPassword());
            assertEquals("updatedPas", changedPasswordTrainee.getUserDTO().getPassword());
        }
        
    }

    static Stream<TraineeDTO> changedPassWord() {
        return Stream.of(
            new TraineeDTO(new UserDTO("John", "Doe", "John.Doe", "randomPassword", true), 
                           LocalDate.of(2000, 12, 9), "Cartagena, Colombia", null),
            new TraineeDTO(new UserDTO("Jane", "Smith", "Jane.Smith", "randomPassword",false), 
                           LocalDate.of(1995, 7, 30), "Bogotá, Colombia", null),
            new TraineeDTO(new UserDTO("Carlos", "Garcia", "Carlos.Garcia", null,true), 
                           LocalDate.of(1988, 5, 20), "Medellín, Colombia",null)
        );
    } 
}
