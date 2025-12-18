package com.rslakra.ratelimiter.slidingwindowlog.persistence.repository;

import com.rslakra.ratelimiter.slidingwindowlog.enums.ShapeType;
import com.rslakra.ratelimiter.slidingwindowlog.payload.response.ShapeResponse;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for shape calculations and history.
 * 
 * @author Rohtash Lakra
 * @created 2/6/24 4:20 PM
 */
public interface ShapeRepository {

    /**
     * Saves a shape calculation result.
     * 
     * @param shapeResponse the shape response to save
     * @return saved shape response
     */
    ShapeResponse save(ShapeResponse shapeResponse);

    /**
     * Finds a shape calculation by ID.
     * 
     * @param id the shape ID
     * @return optional shape response
     */
    Optional<ShapeResponse> findById(String id);

    /**
     * Finds all shape calculations by shape type.
     * 
     * @param shapeType the shape type
     * @return list of shape responses
     */
    List<ShapeResponse> findByShapeType(ShapeType shapeType);

    /**
     * Finds all shape calculations.
     * 
     * @return list of all shape responses
     */
    List<ShapeResponse> findAll();

    /**
     * Deletes a shape calculation by ID.
     * 
     * @param id the shape ID
     * @return true if deleted, false otherwise
     */
    boolean deleteById(String id);

    /**
     * Deletes all shape calculations.
     */
    void deleteAll();

    /**
     * Counts the number of shape calculations.
     * 
     * @return count
     */
    long count();
}
