package com.rslakra.distributedratelimiter.persistence.model;

import lombok.Data;

/**
 * Base class for all geometric shapes.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:20 PM
 */
@Data
public class Shape {

    /**
     * Angle of the shape (in degrees).
     */
    private int angle;

    /**
     * Calculates the perimeter of the shape.
     * 
     * @return perimeter, or null if not applicable
     */
    public Double perimeter() {
        return null;
    }

}
