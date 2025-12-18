package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cuboid (Rectangular Prism) shape implementation.
 * Volume = length * width * height
 * Surface Area = 2 * (length*width + width*height + height*length)
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Cuboid extends Shape {

    private Dimension dimension;
    private Double height;

    /**
     * Calculates the volume of the cuboid.
     * 
     * @return volume
     */
    public Double volume() {
        if (dimension == null || dimension.getLength() == null || 
            dimension.getWidth() == null || height == null) {
            return null;
        }
        if (dimension.getLength() <= 0 || dimension.getWidth() <= 0 || height <= 0) {
            return null;
        }
        return BigDecimal.valueOf(dimension.getLength() * dimension.getWidth() * height)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the surface area of the cuboid.
     * 
     * @return surface area
     */
    public Double surfaceArea() {
        if (dimension == null || dimension.getLength() == null || 
            dimension.getWidth() == null || height == null) {
            return null;
        }
        if (dimension.getLength() <= 0 || dimension.getWidth() <= 0 || height <= 0) {
            return null;
        }
        double l = dimension.getLength();
        double w = dimension.getWidth();
        double h = height;
        return BigDecimal.valueOf(2 * (l * w + w * h + h * l))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
