package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Circle shape implementation.
 * Perimeter = 2 * π * radius
 * Area = π * radius²
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Circle extends RadiusShape {

    /**
     * Calculates the perimeter (circumference) of the circle.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (radius == null || radius <= 0) {
            return null;
        }
        return BigDecimal.valueOf(2 * Math.PI * radius)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the circle.
     * 
     * @return area
     */
    public Double area() {
        if (radius == null || radius <= 0) {
            return null;
        }
        return BigDecimal.valueOf(Math.PI * radius * radius)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
