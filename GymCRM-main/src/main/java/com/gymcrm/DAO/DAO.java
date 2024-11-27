package com.gymcrm.DAO;

import java.util.List;

public interface DAO<T, Id> {

    List<T> getAll();

    void create(T t);
    default void update(T t){
        
    };

}
