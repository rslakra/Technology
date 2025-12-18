package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cube shape implementation.
 * Volume = side³
 * Surface Area = 6 * side²
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:26 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Cube extends Shape {

    private Double side;

    /**
     * Calculates the volume of the cube.
     * 
     * @return volume
     */
    public Double volume() {
        if (side == null || side <= 0) {
            return null;
        }
        return BigDecimal.valueOf(side * side * side)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the surface area of the cube.
     * 
     * @return surface area
     */
    public Double surfaceArea() {
        if (side == null || side <= 0) {
            return null;
        }
        return BigDecimal.valueOf(6 * side * side)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
