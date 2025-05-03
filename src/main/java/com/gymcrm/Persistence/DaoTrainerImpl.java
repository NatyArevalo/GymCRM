package com.gymcrm.Persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gymcrm.DAO.DaoTrainer;
import com.gymcrm.Entities.Trainer;
import com.gymcrm.Entities.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Repository
public class DaoTrainerImpl implements DaoTrainer {
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
    public void create(Trainer trainer){
        connect();
        em.getTransaction().begin();
        em.persist(trainer);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public void update(Trainer trainer){
        connect();
        em.getTransaction().begin();
        em.merge(trainer);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public List<Trainer> getAll(){
        connect();
        ArrayList<Trainer> trainers = (ArrayList<Trainer>) em.createQuery("SELECT a FROM Trainer a", Trainer.class).getResultList();
        disconnect();
        return trainers;
    }

    public Trainer getTrainerById(Long Id) {
        connect();
        Trainer trainer = em.createQuery("SELECT t FROM Trainer WHERE t.id LIKE : id", Trainer.class).setParameter("id", Id).getSingleResult();
        disconnect();
        return trainer;   
    }    

    public Integer validateUsername(String firstname, String lastname){
        connect();
        Integer timesUsernameExist = em.createQuery("SELECT COUNT(t) FROM Trainer WHERE t.firstname LIKE : firstname AND t.lastname LIKE : lastname", Integer.class).setParameter("firstname", firstname).setParameter("lastname", lastname).getSingleResult();
        disconnect();
        return timesUsernameExist;
    }
    
    public TrainingType searchTrainingTypeByName(String name){
        connect();
        TrainingType trainingType = em.createQuery("SELECT t FROM TrainingType t WHERE t.trainingTypeName = :name", TrainingType.class)
        .setParameter("name", name)
        .getSingleResult();
        disconnect();
        return trainingType;
    }

    public Trainer searchTrainerByUsername(String username){
        connect();
        Trainer trainer = (Trainer) em.createQuery("SELECT t FROM Trainer WHERE t.username LIKE : username",Trainer.class);
        disconnect();
        return  trainer;
    }

    public Boolean passwordValidation(String username, String password){
        connect();
        Trainer trainer = (Trainer) em.createQuery("SELECT t FROM Trainer WHERE t.username LIKE : username",Trainer.class);
        disconnect();
        return  trainer.getPassword().equals(password);
    }
}
