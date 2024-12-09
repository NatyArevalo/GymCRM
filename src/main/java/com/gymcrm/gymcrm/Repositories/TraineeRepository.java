package com.gymcrm.gymcrm.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymcrm.gymcrm.Entities.Trainee;
import com.gymcrm.gymcrm.Entities.Trainer;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {

    @Query("SELECT t FROM Trainee t WHERE t.id = :id")
    Trainee getTraineeById(@Param("id") Long id);

    @Query("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username")
    Trainee searchTraineeByUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tt.trainer.id FROM Trainee tr JOIN tr.traineeTrainers tt WHERE tr.user.username = :username)")
    ArrayList<Trainer> searchTrainersNotAssignedToTrainees(@Param("username") String username);

}
