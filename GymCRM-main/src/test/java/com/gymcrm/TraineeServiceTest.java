package com.gymcrm;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.gymcrm.Entities.Trainee;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoTraineeImpl;
import com.gymcrm.Persistence.DaoUserImpl;
import com.gymcrm.Services.TraineeService;
import com.gymcrm.Services.UserService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraineeServiceTest {
    private DaoUserImpl daoUser;
    private UserService userService;
    private DaoTraineeImpl daoTrainee;
    private TraineeService traineeService;

    @BeforeAll
    public void setUp() {

        daoUser = Mockito.mock(DaoUserImpl.class);
        daoTrainee = Mockito.mock(DaoTraineeImpl.class);

   
        userService = new UserService(daoUser);
        traineeService = new TraineeService(daoTrainee, userService);
    }

    @ParameterizedTest
    @MethodSource("provideTrainees")
    public void testCreateTrainee(String firstname, String lastname, String username, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address) {
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

        
        Trainee createdTrainee = traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getId());
        assertEquals(firstname, createdTrainee.getUser().getFirstName());
        assertEquals(lastname, createdTrainee.getUser().getLastName());
        assertEquals(username, createdTrainee.getUser().getUsername());
        assertEquals(Boolean.valueOf(isActive), createdTrainee.getUser().getIsActive());
        assertEquals(LocalDate.of(yearOfBirth,monthOfBirth,dayOfBirth), createdTrainee.getDateOfBirth());
        assertEquals(address, createdTrainee.getAddress());
    }
    static Stream<Object[]> provideTrainees() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia"},
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,7,30,"Cartagena, Colombia"},
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,5,20,"Cartagena, Colombia"}
        );
    }
    @ParameterizedTest
    @MethodSource("updateTrainees")
    public void testUpdateTrainee(String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address, String updatedIsActive, String updatedAddress){

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
    
        when(daoTrainee.searchTraineeByUsername(anyString()))
                .thenReturn(new Trainee(1L, new User(1L, firstname, lastname, username, "password123", Boolean.valueOf(isActive)),
                        LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth), address));
    
        doAnswer(invocation -> {
            Trainee traineeToUpdate = invocation.getArgument(0);
            traineeToUpdate.getUser().setIsActive(Boolean.valueOf(updatedIsActive.isEmpty() ? isActive : updatedIsActive));
            traineeToUpdate.setAddress(updatedAddress.isEmpty() ? address : updatedAddress);
            return null;
        }).when(daoTrainee).update(any(Trainee.class));
    
        Trainee createdTrainee = traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        
        Trainee updatedTrainee = traineeService.updateTrainee(username, createdTrainee.getUser().getPassword(), updatedIsActive, updatedAddress);
        assertNotNull(updatedTrainee);
        assertEquals(createdTrainee.getId(), updatedTrainee.getId());
    
        assertEquals(Boolean.valueOf(updatedIsActive.isEmpty() ? isActive : updatedIsActive), updatedTrainee.getUser().getIsActive());
        assertEquals(updatedAddress.isEmpty() ? address : updatedAddress, updatedTrainee.getAddress());
    
        assertEquals(firstname, updatedTrainee.getUser().getFirstName());
        assertEquals(lastname, updatedTrainee.getUser().getLastName());
            
    }
    static Stream<Object[]> updateTrainees() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", "", "Bogota, Colombia"},
                new Object[]{"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia", "false", ""},
                new Object[]{"John", "Doe", "John.Doe2", "true", 2000,5,20,"Cartagena, Colombia", "", ""}
        );
    }

    @ParameterizedTest
    @MethodSource("deleteTrainees")
    public void testDeleteTrainee(String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address, String usernameToDelete){
       
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
                            

        Trainee createdTrainee = traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        traineeService.deleteTrainee(username, createdTrainee.getUser().getPassword());
        assertNull(traineeService.selectTrainee(usernameToDelete));

    }
    static Stream<Object[]> deleteTrainees() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", "John.Doe"},
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,7,30,"Cartagena, Colombia", "John.Doe"}
                );
    }

    @ParameterizedTest
    @MethodSource("selectTrainees")
    public void testSelectTrainee(String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address,String usernameSelected){
    
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
                            
    
        Trainee createdTrainee = traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);

        when(daoTrainee.searchTraineeByUsername(usernameSelected))
        .thenReturn(createdTrainee);

        Trainee selectedTrainee = traineeService.selectTrainee(usernameSelected);

        if(usernameSelected == null){
            assertNull(selectedTrainee);
        } else if (!username.equals(usernameSelected)) {
            assertNotEquals(createdTrainee, selectedTrainee);
        }else {
            assertEquals(createdTrainee, selectedTrainee);
        }
    }
    static Stream<Object[]> selectTrainees() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", "John.Doe"},
                new Object[]{"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia", null}
        );
    }

    @ParameterizedTest
    @MethodSource("changedPassWord")
    public void testChangedPassword(String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                    Integer monthOfBirth, Integer dayOfBirth, String address){
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
                                    

        Trainee createdTrainee = traineeService.createTrainee(firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        String oldPassword = createdTrainee.getUser().getPassword();

        when(daoUser.passwordValidation(eq(username), eq(createdTrainee.getUser().getPassword()))).thenReturn(true);
        when(daoUser.searchUserByUsername(eq(username))).thenReturn(new User(1L, firstname, lastname, username, "oldPassword", Boolean.valueOf(isActive)));
        when(daoTrainee.searchTraineeByUsername(username)).thenReturn(createdTrainee);

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setPassword("newPassword123"); 
            return null;
        }).when(daoUser).update(any(User.class));

        doAnswer(invocation -> {
            Trainee traineeToUpdate = invocation.getArgument(0);
            traineeToUpdate.getUser().setPassword("newPassword123");
            return null;
        }).when(daoTrainee).update(any(Trainee.class));

        Trainee changedPasswordTrainee = traineeService.changePassword(username, createdTrainee.getUser().getPassword());
        assertNotNull(changedPasswordTrainee);  
        assertNotEquals(oldPassword, changedPasswordTrainee.getUser().getPassword());
        assertEquals("newPassword123", changedPasswordTrainee.getUser().getPassword()); 
     }
    static Stream<Object[]> changedPassWord() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia"},
                new Object[]{"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia"}
        );
    }   
}
