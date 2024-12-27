package com.gymcrm.gymcrm.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymcrm.gymcrm.Entities.Trainer;
import com.gymcrm.gymcrm.Entities.TrainingType;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {

    @Query("SELECT t FROM Trainer t WHERE t.id = :id")
    Trainer getTrainerById(@Param("id") Long id);

    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.username LIKE :username")
    Trainer searchTrainerByUsername(@Param("username") String username);

    @Query("SELECT t FROM TrainingType t WHERE t.trainingTypeName =:trainingTypeName")
    TrainingType searchTrainingTypeByName(@Param("trainingTypeName") String trainingTypeName);

    @Query("SELECT t FROM TrainingType t")
    List<TrainingType> getTrainingTypes();

}
