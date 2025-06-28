package com.rmp.repository;

import java.util.List;

/**
 * 
 * Repository
 * @param <T> is the model to use
 */
public interface Repository<T>  {

    /**
     * Save the data in file.
     * @return true if is successfully saved, otherwise return false.
     */
    boolean saveAll();

    /**
     * Get a list of object from a file.
     * @return the list.
     */
    List<T> findAll();
}
