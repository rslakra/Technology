package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Trapezium (Trapezoid) shape implementation.
 * Perimeter = side1 + side2 + base1 + base2
 * Area = ((base1 + base2) * height) / 2
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:28 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Trapezium extends Base2DShape {

    private Double base1;
    private Double base2;
    private Double side1;
    private Double side2;

    /**
     * Calculates the perimeter of the trapezium.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (base1 == null || base2 == null || side1 == null || side2 == null) {
            return null;
        }
        return BigDecimal.valueOf(base1 + base2 + side1 + side2)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the trapezium.
     * 
     * @return area
     */
    public Double area() {
        if (base1 == null || base2 == null || height == null) {
            return null;
        }
        if (base1 <= 0 || base2 <= 0 || height <= 0) {
            return null;
        }
        return BigDecimal.valueOf(((base1 + base2) * height) / 2.0)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
