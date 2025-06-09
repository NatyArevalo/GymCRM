package com.gymcrm.gymcrm;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "summary"},
        features = "src/test/resources/features",
        glue = "com.gymcrm.gymcrm.StepDefinitions",
        tags = "@component")
public class ComponentTest {
}
