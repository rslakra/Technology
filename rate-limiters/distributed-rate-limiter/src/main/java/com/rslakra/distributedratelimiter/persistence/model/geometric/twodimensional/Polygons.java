package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Regular Polygon shape implementation.
 * Perimeter = number of sides * side length
 * Area = (n * s²) / (4 * tan(π/n)) where n = number of sides, s = side length
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:28 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Polygons extends Shape {

    private Integer numberOfSides; // n
    private Double sideLength; // s
    private Double apothem; // distance from center to midpoint of a side

    /**
     * Calculates the perimeter of the polygon.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (numberOfSides == null || sideLength == null) {
            return null;
        }
        if (numberOfSides < 3 || sideLength <= 0) {
            return null;
        }
        return BigDecimal.valueOf(numberOfSides * sideLength)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the regular polygon.
     * 
     * @return area
     */
    public Double area() {
        if (numberOfSides == null || sideLength == null) {
            return null;
        }
        if (numberOfSides < 3 || sideLength <= 0) {
            return null;
        }
        
        // Area = (n * s²) / (4 * tan(π/n))
        double n = numberOfSides;
        double s = sideLength;
        double area = (n * s * s) / (4 * Math.tan(Math.PI / n));
        
        return BigDecimal.valueOf(area)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
