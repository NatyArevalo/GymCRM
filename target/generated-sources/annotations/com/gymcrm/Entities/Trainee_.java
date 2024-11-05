package com.gymcrm.Entities;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(Trainee.class)
public abstract class Trainee_ {

	public static final String ADDRESS = "address";
	public static final String DATE_OF_BIRTH = "dateOfBirth";
	public static final String ID = "id";
	public static final String USER = "user";

	
	/**
	 * @see com.gymcrm.Entities.Trainee#address
	 **/
	public static volatile SingularAttribute<Trainee, String> address;
	
	/**
	 * @see com.gymcrm.Entities.Trainee#dateOfBirth
	 **/
	public static volatile SingularAttribute<Trainee, LocalDate> dateOfBirth;
	
	/**
	 * @see com.gymcrm.Entities.Trainee#id
	 **/
	public static volatile SingularAttribute<Trainee, Long> id;
	
	/**
	 * @see com.gymcrm.Entities.Trainee
	 **/
	public static volatile EntityType<Trainee> class_;
	
	/**
	 * @see com.gymcrm.Entities.Trainee#user
	 **/
	public static volatile SingularAttribute<Trainee, User> user;

}

