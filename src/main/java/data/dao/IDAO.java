package main.java.data.dao;

import java.util.List;


public interface IDAO<T> {

    void update(T t);
    void delete(T t);
    int create(T t);

    List<T> listAll();
    T getByID(int id);

}

