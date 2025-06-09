package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.DTO.UserDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
public class CommonTraineeSteps {

    private final TraineeContext traineeContext;

    public CommonTraineeSteps(TraineeContext traineeContext) {
        this.traineeContext = traineeContext;
    }

    @Given("a trainee with name {string} and lastname {string}")
    public void a_trainee_with_name_and_lastname(String name, String lastName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(name);
        userDTO.setLastName(lastName);
        traineeContext.getTraineeDTO().setUserDTO(userDTO);
    }

    @And("the date of birth is {string} and the address is {string}")
    public void the_date_of_birth_is_and_the_address_is(String dateOfBirth, String address) {
        traineeContext.getTraineeDTO().setDateOfBirth(LocalDate.parse(dateOfBirth));
        traineeContext.getTraineeDTO().setAddress(address);
    }
}
