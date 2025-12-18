package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Sphere shape implementation.
 * Volume = (4/3) * π * radius³
 * Surface Area = 4 * π * radius²
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:26 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Sphere extends Base3DShape {

    /**
     * Calculates the volume of the sphere.
     * 
     * @return volume
     */
    public Double volume() {
        if (radius == null || radius <= 0) {
            return null;
        }
        return BigDecimal.valueOf((4.0 / 3.0) * Math.PI * radius * radius * radius)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the surface area of the sphere.
     * 
     * @return surface area
     */
    public Double surfaceArea() {
        if (radius == null || radius <= 0) {
            return null;
        }
        return BigDecimal.valueOf(4 * Math.PI * radius * radius)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
