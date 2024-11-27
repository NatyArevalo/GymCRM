package com.gymcrm.Entities;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(Training.class)
public abstract class Training_ {

	public static final String TRAINING_TYPE = "trainingType";
	public static final String DURATION = "duration";
	public static final String TRAINING_DATE = "trainingDate";
	public static final String TRAINING_NAME = "trainingName";
	public static final String TRAINER = "trainer";
	public static final String ID = "id";
	public static final String TRAINEE = "trainee";

	
	/**
	 * @see com.gymcrm.Entities.Training#trainingType
	 **/
	public static volatile SingularAttribute<Training, TrainingType> trainingType;
	
	/**
	 * @see com.gymcrm.Entities.Training#duration
	 **/
	public static volatile SingularAttribute<Training, Double> duration;
	
	/**
	 * @see com.gymcrm.Entities.Training#trainingDate
	 **/
	public static volatile SingularAttribute<Training, LocalDate> trainingDate;
	
	/**
	 * @see com.gymcrm.Entities.Training#trainingName
	 **/
	public static volatile SingularAttribute<Training, String> trainingName;
	
	/**
	 * @see com.gymcrm.Entities.Training#trainer
	 **/
	public static volatile SingularAttribute<Training, Trainer> trainer;
	
	/**
	 * @see com.gymcrm.Entities.Training#id
	 **/
	public static volatile SingularAttribute<Training, Long> id;
	
	/**
	 * @see com.gymcrm.Entities.Training#trainee
	 **/
	public static volatile SingularAttribute<Training, Trainee> trainee;
	
	/**
	 * @see com.gymcrm.Entities.Training
	 **/
	public static volatile EntityType<Training> class_;

}

