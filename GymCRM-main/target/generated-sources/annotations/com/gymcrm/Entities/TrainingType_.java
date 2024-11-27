package com.gymcrm.Entities;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TrainingType.class)
public abstract class TrainingType_ {

	public static final String TRAINING_TYPE_NAME = "trainingTypeName";
	public static final String ID = "id";

	
	/**
	 * @see com.gymcrm.Entities.TrainingType#trainingTypeName
	 **/
	public static volatile SingularAttribute<TrainingType, String> trainingTypeName;
	
	/**
	 * @see com.gymcrm.Entities.TrainingType#id
	 **/
	public static volatile SingularAttribute<TrainingType, Long> id;
	
	/**
	 * @see com.gymcrm.Entities.TrainingType
	 **/
	public static volatile EntityType<TrainingType> class_;

}

