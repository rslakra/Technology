package com.rslakra.distributedratelimiter.persistence.model.geometric.threedimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for 3D shapes that use radius and height for calculations.
 * This class provides common properties that are reused across multiple 3D shapes.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class Base3DShape extends Shape {

    /**
     * Radius of the shape.
     */
    protected Double radius;

    /**
     * Height of the shape.
     */
    protected Double height;
}

