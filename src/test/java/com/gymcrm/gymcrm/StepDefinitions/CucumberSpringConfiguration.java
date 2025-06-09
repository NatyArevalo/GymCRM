package com.gymcrm.gymcrm.StepDefinitions;

import com.gymcrm.gymcrm.Filter.JwtAuthenticationFilter;
import com.gymcrm.gymcrm.Services.JwtService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {
    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

}
