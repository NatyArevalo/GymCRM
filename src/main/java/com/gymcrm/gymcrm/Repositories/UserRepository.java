package com.gymcrm.gymcrm.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymcrm.gymcrm.Entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.username  = :username")
    User searchUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    User passwordValidation(@Param("username") String username, @Param("password") String password);

    @Query("SELECT COUNT(u) FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    Long validateUsername(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
