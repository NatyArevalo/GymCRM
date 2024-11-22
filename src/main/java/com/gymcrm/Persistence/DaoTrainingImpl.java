package com.gymcrm.Persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gymcrm.DAO.DaoTraining;
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

    public ArrayList<Training> searchTrainingsByTrainersName(String name, LocalDate fromDate, LocalDate toDate, String traineeName){
        connect();
        ArrayList<Training> trainings = (ArrayList<Training>) em.createQuery(
            "SELECT a FROM Training a " +
            "JOIN a.trainer b " + 
            "JOIN a.trainee c " + 
            "WHERE b.name LIKE : name "+
            "AND a.trainingDate  BETWEEN : fromDate "+
            "AND : toDate AND "+
            "c.name LIKE : traineeName", Training.class)
            .setParameter("name", name)
            .setParameter("fromDate", fromDate).setParameter("toDate", toDate)
            .setParameter("traineeName", traineeName).getResultList();
        disconnect();
        return trainings;
    }

    public ArrayList<Training> searchTrainingsByTraineesName(String name, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType){
        connect();
        ArrayList<Training> trainings = (ArrayList<Training>) em.createQuery(
            "SELECT a FROM Training a " +
            "JOIN a.trainee b " + 
            "JOIN a.trainer c " + 
            "JOIN a.trainingType d"+
            "WHERE b.name LIKE : name "+
            "AND a.trainingDate  BETWEEN : fromDate "+
            "AND : toDate AND "+
            "c.name LIKE : trainerName"+
            "d.trainingType LIKE : trainingType", Training.class)
            .setParameter("name", name)
            .setParameter("fromDate", fromDate).setParameter("toDate", toDate)
            .setParameter("traineeName", trainerName)
            .setParameter("trainingType", trainingType).getResultList();
        disconnect();
        return trainings;

    }
    

}
