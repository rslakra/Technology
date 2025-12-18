package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rhombus shape implementation.
 * Perimeter = 4 * side
 * Area = (diagonal1 * diagonal2) / 2 or base * height
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:28 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Rhombus extends Base2DShape {

    private Double side;
    private Double diagonal1;
    private Double diagonal2;

    /**
     * Calculates the perimeter of the rhombus.
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
     * Calculates the area of the rhombus.
     * 
     * @return area
     */
    public Double area() {
        // Try diagonal method first
        if (diagonal1 != null && diagonal2 != null && diagonal1 > 0 && diagonal2 > 0) {
            return BigDecimal.valueOf((diagonal1 * diagonal2) / 2.0)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        }
        // Fall back to base * height
        if (base != null && height != null && base > 0 && height > 0) {
            return BigDecimal.valueOf(base * height)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        }
        return null;
    }
}
