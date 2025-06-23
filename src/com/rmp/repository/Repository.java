package com.rmp.repository;

import java.util.List;

// TODO each repository must have is own file
/**
 * 
 * Repository
 * @param <T> is the model to use
 */
public interface Repository<T>  {
    boolean saveAll();

    List<T> findAll();
}
