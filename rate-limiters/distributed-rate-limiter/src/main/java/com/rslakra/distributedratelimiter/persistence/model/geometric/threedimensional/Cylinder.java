package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cylinder shape implementation.
 * Volume = π * radius² * height
 * Surface Area = 2 * π * radius * (radius + height)
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Cylinder extends Base3DShape {

    /**
     * Calculates the volume of the cylinder.
     * 
     * @return volume
     */
    public Double volume() {
        if (radius == null || height == null) {
            return null;
        }
        if (radius <= 0 || height <= 0) {
            return null;
        }
        return BigDecimal.valueOf(Math.PI * radius * radius * height)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the surface area of the cylinder.
     * 
     * @return surface area
     */
    public Double surfaceArea() {
        if (radius == null || height == null) {
            return null;
        }
        if (radius <= 0 || height <= 0) {
            return null;
        }
        return BigDecimal.valueOf(2 * Math.PI * radius * (radius + height))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
