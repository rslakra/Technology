package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Kite shape implementation.
 * Perimeter = 2 * (side1 + side2)
 * Area = (diagonal1 * diagonal2) / 2
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:28 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Kite extends Shape {

    private Double side1;
    private Double side2;
    private Double diagonal1;
    private Double diagonal2;

    /**
     * Calculates the perimeter of the kite.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (side1 == null || side2 == null) {
            return null;
        }
        if (side1 <= 0 || side2 <= 0) {
            return null;
        }
        return BigDecimal.valueOf(2 * (side1 + side2))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the kite.
     * 
     * @return area
     */
    public Double area() {
        if (diagonal1 == null || diagonal2 == null) {
            return null;
        }
        if (diagonal1 <= 0 || diagonal2 <= 0) {
            return null;
        }
        return BigDecimal.valueOf((diagonal1 * diagonal2) / 2.0)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
