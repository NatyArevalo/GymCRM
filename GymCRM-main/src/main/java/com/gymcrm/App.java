package com.gymcrm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gymcrm.Configurations.AppConfiguration;
import com.gymcrm.Configurations.GymFacade;
import com.gymcrm.Entities.Trainer;

public class App {
    public static void main( String[] args ){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        
        GymFacade gymFacade = context.getBean(GymFacade.class);

        Trainer newTrainer = gymFacade.createTrainer("John", "Doe",  "true", "FUNCTIONAL");
        System.out.println("Created Trainer: " + newTrainer.toString());
        
        Trainer newTrainer2 =  gymFacade.createTrainer("John", "Doe",  "true", "FUNCTIONAL");
        System.out.println("Created Trainer: " + newTrainer2.toString());

    }
    
}
