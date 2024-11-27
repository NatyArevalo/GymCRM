package com.gymcrm.Persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gymcrm.DAO.DaoUser;
import com.gymcrm.Entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Repository
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
        ArrayList<User> user = (ArrayList<User>) em.createQuery("SELECT a FROM Users a", User.class).getResultList();
        disconnect();
        return user;
    }

    public Integer validateUsername(String firstname, String lastname){
        connect();
        Integer timesUsernameExist = em.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.firstname LIKE :firstname AND u.lastname LIKE :lastname", 
            Long.class  
        )
        .setParameter("firstname", firstname)
        .setParameter("lastname", lastname)
        .getSingleResult()
        .intValue();
        disconnect();
        return timesUsernameExist;
    }

     public User searchUserByUsername(String username){
        connect();
        User user = (User) em.createQuery("SELECT u FROM User WHERE u.username LIKE : username",User.class);
        disconnect();
        return  user;
    }

    public Boolean passwordValidation(String username, String password){
        connect();
        User user = (User) em.createQuery("SELECT u FROM User WHERE u.username LIKE : username",User.class);
        disconnect();
        return  user.getPassword().equals(password);
    }

}
