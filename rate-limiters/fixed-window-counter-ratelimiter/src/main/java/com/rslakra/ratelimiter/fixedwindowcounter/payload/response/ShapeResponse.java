package com.rslakra.ratelimiter.fixedwindowcounter.payload.response;

import com.rslakra.ratelimiter.fixedwindowcounter.enums.ShapeType;
import lombok.Data;

/**
 * Response model for shape calculations.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:46 PM
 */
@Data
public class ShapeResponse {

    private ShapeType shape;
    private Double perimeter; // For 2D shapes
    private Double area; // For 2D shapes
    private Double volume; // For 3D shapes
    private Double surfaceArea; // For 3D shapes
}
