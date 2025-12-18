package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for 2D shapes that use radius for calculations.
 * This class provides common properties that are reused across shapes like Circle.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class RadiusShape extends Shape {

    /**
     * Radius of the shape.
     */
    protected Double radius;
}

