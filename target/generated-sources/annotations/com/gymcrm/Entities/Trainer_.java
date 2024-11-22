package com.gymcrm.Entities;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Trainer.class)
public abstract class Trainer_ {

	public static final String SPECIALIZATION = "specialization";
	public static final String ID = "id";
	public static final String USER = "user";

	
	/**
	 * @see com.gymcrm.Entities.Trainer#specialization
	 **/
	public static volatile SingularAttribute<Trainer, TrainingType> specialization;
	
	/**
	 * @see com.gymcrm.Entities.Trainer#id
	 **/
	public static volatile SingularAttribute<Trainer, Long> id;
	
	/**
	 * @see com.gymcrm.Entities.Trainer
	 **/
	public static volatile EntityType<Trainer> class_;
	
	/**
	 * @see com.gymcrm.Entities.Trainer#user
	 **/
	public static volatile SingularAttribute<Trainer, User> user;

}

