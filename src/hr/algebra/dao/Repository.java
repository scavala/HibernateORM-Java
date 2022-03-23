/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import java.util.List;

/**
 *
 * @author daniel.bele
 */
public interface Repository<T> {

    int add(T data) throws Exception;
    void delete(T item) throws Exception;
    List<T> getAll() throws Exception;
    T get(int idItem) throws Exception;
    void update(T item) throws Exception;
    
    default void release() throws Exception{};
}
