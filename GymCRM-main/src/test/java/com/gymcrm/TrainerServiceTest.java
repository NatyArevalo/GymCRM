package com.gymcrm;

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
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.TrainingType;
import com.gymcrm.Entities.User;
import com.gymcrm.Persistence.DaoTrainerImpl;
import com.gymcrm.Persistence.DaoUserImpl;
import com.gymcrm.Services.TrainerService;
import com.gymcrm.Services.UserService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerServiceTest {
    
    private UserService userService;
    private DaoUserImpl daoUser;
    private DaoTrainerImpl daoTrainer;
    private TrainerService trainerService;

    @BeforeAll
    public void setUp() {
        daoUser = Mockito.mock(DaoUserImpl.class);
        daoTrainer = Mockito.mock(DaoTrainerImpl.class);
        userService = new UserService(daoUser);
        trainerService = new TrainerService(daoTrainer, userService);
    }

    @ParameterizedTest
    @MethodSource("provideTrainers")
    public void testCreateTrainer(String firstName, String lastName, String username, Boolean isActive, String specialization) {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0); 
            user.setId(1L); 
            return null; 
        }).when(daoUser).create(any(User.class));
        
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0); 
            trainer.setId(1L);
            return null; 
        }).when(daoTrainer).create(any(Trainer.class));

        when(daoTrainer.searchTrainingTypeByName(anyString()))
            .thenAnswer(invocation -> {
                String specializationName = invocation.getArgument(0);
                return new TrainingType(1L, specializationName);
            });

        Trainer createdTrainer = trainerService.createTrainer(firstName, lastName, String.valueOf(isActive), specialization);
        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getId());
        assertEquals(firstName, createdTrainer.getUser().getFirstName());
        assertEquals(lastName, createdTrainer.getUser().getLastName());
        assertEquals(username, createdTrainer.getUser().getUsername());
        assertEquals(isActive, createdTrainer.getUser().getIsActive());
        assertEquals(specialization, createdTrainer.getSpecialization().getTrainingTypeName());

    }
    static Stream<Object[]> provideTrainers() {
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", true, "FUNCTIONAL"},
                new Object[]{"John", "Doe", "John.Doe", true, "STRENGTH"},
                new Object[]{"John", "Doe", "John.Doe", true, "AGILITY"}
        );
    }

    @ParameterizedTest
    @MethodSource("updateTrainers")
    public void testUpdateTrainer(String firstName, String lastName, String username, String isActive,
                                  String specialization, String updatedIsActive, String updatedSpecialization){
    
        doAnswer(invocation -> {
            User user = invocation.getArgument(0); 
            user.setId(1L); 
            return null; 
        }).when(daoUser).create(any(User.class));
        
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0); 
            trainer.setId(1L);
            return null; 
        }).when(daoTrainer).create(any(Trainer.class));

        Trainer createdTrainer = trainerService.createTrainer(firstName, lastName, String.valueOf(isActive), specialization);
        assertNotNull(createdTrainer);

        when(daoTrainer.searchTrainerByUsername(anyString()))
         .thenReturn(new Trainer(1L, new User(1L, firstName, lastName, username, "password123", Boolean.valueOf(isActive)),
                        new TrainingType(1L, specialization)));
    
        doAnswer(invocation -> {
            Trainer trainerToUpdate = invocation.getArgument(0);
            trainerToUpdate.getUser().setIsActive(Boolean.valueOf(updatedIsActive.isEmpty() ? isActive : updatedIsActive));
            trainerToUpdate.setSpecialization(new TrainingType(1L, updatedSpecialization.isEmpty() ? specialization : updatedSpecialization));
            return null;
        }).when(daoTrainer).update(any(Trainer.class));

        Trainer updatedTrainer = trainerService.updateTrainer(username, createdTrainer.getUser().getPassword(), updatedIsActive, updatedSpecialization);
        assertNotNull(updatedTrainer);
        assertEquals(username, updatedTrainer.getUser().getUsername());

        if (updatedIsActive.isEmpty()){
            assertEquals(Boolean.valueOf(isActive), updatedTrainer.getUser().getIsActive());
        }else {
            assertEquals(Boolean.valueOf(updatedIsActive), updatedTrainer.getUser().getIsActive());
        }
        if (updatedSpecialization.isEmpty()){
            assertEquals(specialization, updatedTrainer.getSpecialization().getTrainingTypeName());
        }else {
            assertEquals(updatedSpecialization, updatedTrainer.getSpecialization().getTrainingTypeName());
        }

        assertEquals(firstName, updatedTrainer.getUser().getFirstName());
        assertEquals(lastName, updatedTrainer.getUser().getLastName());

    }

    static  Stream<Object[]> updateTrainers(){
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true", "FUNCTIONAL", "","STRENGTH"},
                new Object[]{"Jane", "Doe", "Jane.Doe", "false", "STRENGTH", "false", ""}
        );
    }

    @ParameterizedTest
    @MethodSource("selectTrainers")
    public void testSelectTrainer(String firstName, String lastName, String username, String isActive, String specialization, String selectedUsername) {

        doAnswer(invocation -> {
            User user = invocation.getArgument(0); 
            user.setId(1L); 
            return null; 
        }).when(daoUser).create(any(User.class));
        
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0); 
            trainer.setId(1L);
            return null; 
        }).when(daoTrainer).create(any(Trainer.class));
   
        Trainer createdTrainer = trainerService.createTrainer(firstName, lastName, isActive, specialization);
        assertNotNull(createdTrainer);

        when(daoTrainer.searchTrainerByUsername(anyString()))
        .thenReturn(new Trainer(1L, new User(1L, firstName, lastName, username, createdTrainer.getUser().getPassword(), Boolean.valueOf(isActive)),
                       new TrainingType(1L, specialization)));

        Trainer selectedTrainer = trainerService.selectTrainer(selectedUsername);

        if(selectedUsername == null){
            assertNull(selectedTrainer);
        } else if (!username.equals(selectedUsername)) {
            assertNotEquals(createdTrainer, selectedTrainer);
        }else {
            assertEquals(createdTrainer.getUser().getUsername(), selectedTrainer.getUser().getUsername());
            assertEquals(createdTrainer.getUser().getPassword(), selectedTrainer.getUser().getPassword());
        }


    }

    static  Stream<Object[]> selectTrainers(){
        return Stream.of(
                new Object[]{"John", "Doe", "John.Doe", "true","FUNCTIONAL","John.Doe"},
                new Object[]{"Jane", "Doe", "Jane.Doe", "false", "STRENGTH", "Jane.Doe"},
                new Object[]{"John", "Doe", "John.Doe1", "true", "AGILITY",null}
        );
    }

    @ParameterizedTest
    @MethodSource("changedPassWord")
    public void testChangedPassword(String firstname, String lastname, String username, String isActive, String specialization){
        doAnswer(invocation -> {
            User user = invocation.getArgument(0); 
            user.setId(1L); 
            return null; 
        }).when(daoUser).create(any(User.class));
        
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0); 
            trainer.setId(1L);
            return null; 
        }).when(daoTrainer).create(any(Trainer.class));
                                    

        Trainer createdTrainer = trainerService.createTrainer(firstname, lastname, isActive, specialization);
        assertNotNull(createdTrainer);
        String oldPassword = createdTrainer.getUser().getPassword();

        when(daoUser.passwordValidation(username, createdTrainer.getUser().getPassword())).thenReturn(true);
        when(daoUser.searchUserByUsername(username)).thenReturn(new User(1L, firstname, lastname, username, "oldPassword", Boolean.valueOf(isActive)));
        when(daoTrainer.searchTrainerByUsername(username)).thenReturn(createdTrainer);

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setPassword("newPassword123"); 
            return null;
        }).when(daoUser).update(any(User.class));

        doAnswer(invocation -> {
            Trainer trainerToUpdate = invocation.getArgument(0);
            trainerToUpdate.getUser().setPassword("newPassword123");
            return null;
        }).when(daoTrainer).update(any(Trainer.class));

        Trainer changedPasswordTrainer = trainerService.changePassword(username, createdTrainer.getUser().getPassword());
        assertNotNull(changedPasswordTrainer);  
        assertNotEquals(oldPassword, changedPasswordTrainer.getUser().getPassword());
        assertEquals("newPassword123", changedPasswordTrainer.getUser().getPassword()); 
     }
    static Stream<Object[]> changedPassWord() {
        return Stream.of(
            new Object[]{"John", "Doe", "John.Doe", "true", "FUNCTIONAL"},
            new Object[]{"John", "Doe", "John.Doe", "true", "STRENGTH"},
            new Object[]{"John", "Doe", "John.Doe", "true", "AGILITY"}
        );
    }   
}
