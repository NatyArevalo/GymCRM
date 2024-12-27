package com.gymcrm.gymcrm.Repositories;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gymcrm.gymcrm.Entities.Training;

public interface TrainingRepository extends CrudRepository<Training, Long>{

    @Query("SELECT a FROM Training a JOIN a.trainer b JOIN a.trainee c JOIN b.user u JOIN c.user ut WHERE u.username LIKE :username AND a.trainingDate  BETWEEN :fromDate AND :toDate AND ut.username LIKE :traineeName")
    ArrayList<Training> searchTrainerTrainingsByCriteria(@Param("username") String username, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("traineeName") String traineeName);

    @Query("SELECT a FROM Training a JOIN a.trainee b JOIN a.trainer c JOIN b.user u JOIN a.trainingType d JOIN c.user ut WHERE u.username LIKE :username AND a.trainingDate  BETWEEN :fromDate AND :toDate AND ut.username LIKE :trainerName AND d.trainingTypeName LIKE :trainingType")
    ArrayList<Training> searchTraineeTrainingsByCriteria(@Param("username") String username, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("trainerName") String trainerName, @Param("trainingType") String trainingType);
   

}
