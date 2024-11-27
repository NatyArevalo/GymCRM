package com.gymcrm.Persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gymcrm.DAO.DaoTrainee;
import com.gymcrm.Entities.Trainee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Repository
public class DaoTraineeImpl implements DaoTrainee {
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
    public void create(Trainee trainee){
        connect();
        em.getTransaction().begin();
        em.persist(trainee);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public void update(Trainee trainee){
        connect();
        em.getTransaction().begin();
        em.merge(trainee);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public List<Trainee> getAll(){
        connect();
        ArrayList<Trainee> trainee = (ArrayList<Trainee>) em.createQuery("SELECT a FROM Trainee a", Trainee.class).getResultList();
        disconnect();
        return trainee;
    }

    public Trainee getTraineeById(Long Id) {
        connect();
        Trainee trainee = em.createQuery("SELECT t FROM Trainee WHERE t.id LIKE : id", Trainee.class).setParameter("id", Id).getSingleResult();
        disconnect();
        return trainee;   
    }    

    public void delete(Trainee trainee){
        connect();
        em.getTransaction().begin();
        em.remove(em.contains(trainee) ? trainee : em.merge(trainee));
        em.getTransaction().commit();
        disconnect();
    }

    public Trainee searchTraineeByUsername(String username){
        connect();
        Trainee trainee = (Trainee) em.createQuery("SELECT t FROM Trainer t " + 
        "JOIN t.user u"+
        "WHERE u.username LIKE : username",Trainee.class)
        .setParameter("username", username);
        disconnect();
        return trainee;
    }

}
