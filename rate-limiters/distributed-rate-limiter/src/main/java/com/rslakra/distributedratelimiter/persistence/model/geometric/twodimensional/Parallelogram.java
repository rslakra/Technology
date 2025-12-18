package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Parallelogram shape implementation.
 * Perimeter = 2 * (length + width)
 * Area = base * height
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:27 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Parallelogram extends Base2DShape {

    private Dimension dimension;

    /**
     * Calculates the perimeter of the parallelogram.
     * 
     * @return perimeter
     */
    public Double perimeter() {
        if (dimension == null || dimension.getLength() == null || dimension.getWidth() == null) {
            return null;
        }
        return BigDecimal.valueOf(2 * (dimension.getLength() + dimension.getWidth()))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * Calculates the area of the parallelogram.
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
        return BigDecimal.valueOf(base * height)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
