package com.rslakra.ratelimiter.tokenbucket.payload.request;

import com.rslakra.ratelimiter.tokenbucket.enums.ShapeType;
import com.rslakra.ratelimiter.tokenbucket.persistence.model.Dimension;
import lombok.Data;

/**
 * Request model for shape calculations.
 * Supports both angle-based and dimension-based calculations depending on shape type.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:45 PM
 */
@Data
public class ShapeRequest {

    /**
     * Type of shape to calculate.
     */
    private ShapeType shape;

    /**
     * Dimensions of the shape (length, width, radius, sides, etc.).
     */
    private Dimension dimension;

    /**
     * Angle of the shape in degrees (optional, used for polygons and angle-based calculations).
     */
    private Integer angle;

}
