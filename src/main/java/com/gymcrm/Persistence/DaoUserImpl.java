package com.gymcrm.Persistence;

import java.util.ArrayList;
import java.util.List;

import com.gymcrm.DAO.DaoUser;
import com.gymcrm.Entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DaoUserImpl implements DaoUser{
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
    public void create(User user){
        connect();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public void update(User user){
        connect();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        disconnect();
    }

    @Override
    public List<User> getAll(){
        connect();
        ArrayList<User> user = (ArrayList<User>) em.createQuery("SELECT a FROM User a", User.class).getResultList();
        disconnect();
        return user;
    }

}
