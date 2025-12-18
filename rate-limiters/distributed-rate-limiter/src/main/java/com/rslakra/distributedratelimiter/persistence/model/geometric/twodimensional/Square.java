package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Square shape implementation.
 * Perimeter = 4 * side
 * Area = side²
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Square extends Shape {

    private Double side;

    /**
     * Calculates the perimeter of the square.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (side == null || side <= 0) {
            return null;
        }
        return BigDecimal.valueOf(4 * side)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the square.
     * 
     * @return area
     */
    public Double area() {
        if (side == null || side <= 0) {
            return null;
        }
        return BigDecimal.valueOf(side * side)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
