package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cone shape implementation.
 * Volume = (1/3) * π * radius² * height
 * Surface Area = π * radius * (radius + slantHeight) or π * radius * (radius + √(radius² + height²))
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:26 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Cone extends Base3DShape {

    private Double slantHeight;

    /**
     * Calculates the volume of the cone.
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
        return BigDecimal.valueOf((1.0 / 3.0) * Math.PI * radius * radius * height)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the surface area of the cone.
     * 
     * @return surface area
     */
    public Double surfaceArea() {
        if (radius == null || radius <= 0) {
            return null;
        }
        
        double slant;
        if (slantHeight != null && slantHeight > 0) {
            slant = slantHeight;
        } else if (height != null && height > 0) {
            // Calculate slant height: √(radius² + height²)
            slant = Math.sqrt(radius * radius + height * height);
        } else {
            return null;
        }
        
        return BigDecimal.valueOf(Math.PI * radius * (radius + slant))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
