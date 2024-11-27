package com.gymcrm.Persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gymcrm.DAO.DaoTraining;
import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.Training;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


@Repository
public class DaoTrainingImpl implements DaoTraining{
    private final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("JPAPU");
    private EntityManager em = EMF.createEntityManager();

    public void connect(){
        if(!em.isOpen()){
            em = EMF.createEntityManager();
        }
    }

    public void disconnect(){
        if(em.isOpen()){
            em.close();
        }
    }

    @Override
    public void create(Training training){
        connect();
        em.getTransaction().begin();
        em.persist(training);
        em.getTransaction().commit();
        disconnect();
    }

   @Override
    public List<Training> getAll(){
        connect();
        ArrayList<Training> trainings = (ArrayList<Training>) em.createQuery("SELECT a FROM Training a", Training.class).getResultList();
        disconnect();
        return trainings;
    }

    public ArrayList<Training> searchTrainingsByTrainersName(String username, LocalDate fromDate, LocalDate toDate, String traineeName){
        connect();
        ArrayList<Training> trainings = (ArrayList<Training>) em.createQuery(
            "SELECT a FROM Training a " +
            "JOIN a.trainer b " + 
            "JOIN a.trainee c " + 
            "JOIN b.user u"+
            "WHERE u.username LIKE :username "+
            "AND a.trainingDate  BETWEEN :fromDate "+
            "AND :toDate AND "+
            "c.username LIKE : traineeName", Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate).setParameter("toDate", toDate)
            .setParameter("traineeName", traineeName).getResultList();
        disconnect();
        return trainings;
    }

    public ArrayList<Training> searchTrainingsByTraineesName(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType){
        connect();
        ArrayList<Training> trainings = (ArrayList<Training>) em.createQuery(
            "SELECT a FROM Training a " +
            "JOIN a.trainee b " + 
            "JOIN a.trainer c " + 
            "JOIN b.user u"+
            "JOIN a.trainingType d"+
            "WHERE u.username LIKE : username "+
            "AND a.trainingDate  BETWEEN : fromDate "+
            "AND : toDate AND "+
            "c.username LIKE : trainerName"+
            "d.trainingType LIKE : trainingType", Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate).setParameter("toDate", toDate)
            .setParameter("trainerName", trainerName)
            .setParameter("trainingType", trainingType).getResultList();
        disconnect();
        return trainings;

    }

    public ArrayList<Trainer> searchTrainersNotAssignedToTrainees(String username){
        connect();
        ArrayList<Trainer> trainers = (ArrayList<Trainer>) em.createQuery("SELECT t " +
        "FROM Trainer t " +
        "WHERE t.id NOT IN (" +
        "SELECT tr.trainer.id " +
        " FROM Training tr " +
        " WHERE tr.trainee.user.username = :username" +")"
        ,Trainer.class) .setParameter("username", username)
        .getResultList();
        disconnect();
        return trainers;
    }
    

}
