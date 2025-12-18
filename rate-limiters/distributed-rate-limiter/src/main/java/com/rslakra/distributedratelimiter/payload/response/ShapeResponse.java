package com.rslakra.distributedratelimiter.payload.response;

import com.rslakra.distributedratelimiter.enums.ShapeType;
import lombok.Data;

/**
 * Response DTO for shape calculations.
 * Contains shape type and calculated properties (perimeter, area, volume, surface area).
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:46 PM
 */
@Data
public class ShapeResponse {

    private ShapeType shape;
    
    // 2D shape properties
    private Double perimeter;
    private Double area;
    
    // 3D shape properties
    private Double volume;
    private Double surfaceArea;
}
