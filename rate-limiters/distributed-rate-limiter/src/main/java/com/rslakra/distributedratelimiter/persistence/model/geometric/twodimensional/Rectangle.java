package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rohtash Lakra
 * @created 4/14/23 3:18 PM
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Rectangle extends Shape {

    private Dimension dimension;

    /**
     * Calculates the perimeter of the rectangle.
     * 
     * @return perimeter, or null if dimension is invalid
     */
    public Double perimeter() {
        if (dimension == null || dimension.getLength() == null || dimension.getWidth() == null) {
            return null;
        }
        if (dimension.getLength() <= 0 || dimension.getWidth() <= 0) {
            return null;
        }
        return BigDecimal.valueOf(2 * (dimension.getLength() + dimension.getWidth()))
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
