package com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional;

import com.rslakra.distributedratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for 2D shapes that use base and height for area calculations.
 * This class provides common properties that are reused across multiple shapes.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class Base2DShape extends Shape {

    /**
     * Base length of the shape.
     */
    protected Double base;

    /**
     * Height (perpendicular distance) of the shape.
     */
    protected Double height;
}

