package com.gymcrm.Entities;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PASSWORD = "password";
	public static final String ID = "id";
	public static final String IS_ACTIVE = "isActive";
	public static final String USERNAME = "username";

	
	/**
	 * @see com.gymcrm.Entities.User#firstName
	 **/
	public static volatile SingularAttribute<User, String> firstName;
	
	/**
	 * @see com.gymcrm.Entities.User#lastName
	 **/
	public static volatile SingularAttribute<User, String> lastName;
	
	/**
	 * @see com.gymcrm.Entities.User#password
	 **/
	public static volatile SingularAttribute<User, String> password;
	
	/**
	 * @see com.gymcrm.Entities.User#id
	 **/
	public static volatile SingularAttribute<User, Long> id;
	
	/**
	 * @see com.gymcrm.Entities.User#isActive
	 **/
	public static volatile SingularAttribute<User, Boolean> isActive;
	
	/**
	 * @see com.gymcrm.Entities.User
	 **/
	public static volatile EntityType<User> class_;
	
	/**
	 * @see com.gymcrm.Entities.User#username
	 **/
	public static volatile SingularAttribute<User, String> username;

}

