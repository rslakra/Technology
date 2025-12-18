package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Triangle shape implementation.
 * Perimeter = side1 + side2 + side3
 * Area = (base * height) / 2
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Triangle extends Base2DShape {

    private Double side1;
    private Double side2;
    private Double side3;

    /**
     * Calculates the perimeter of the triangle.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (side1 == null || side2 == null || side3 == null) {
            return null;
        }
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) {
            return null;
        }
        return BigDecimal.valueOf(side1 + side2 + side3)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the triangle.
     * 
     * @return area
     */
    public Double area() {
        if (base == null || height == null) {
            return null;
        }
        if (base <= 0 || height <= 0) {
            return null;
        }
        return BigDecimal.valueOf((base * height) / 2.0)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
