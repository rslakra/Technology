package com.rslakra.ratelimiter.leakybucket.persistence.model;

import lombok.Data;

/**
 * Dimension model supporting various geometric measurements.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:20 PM
 */
@Data
public class Dimension {

    // 2D dimensions
    private Double length;
    private Double width;
    private Double height; // For triangles, trapezoids
    private Double radius; // For circles
    private Double side; // For squares, equilateral shapes
    private Double base; // For triangles, parallelograms
    private Double side1; // For triangles, kites
    private Double side2; // For triangles, kites
    private Double side3; // For triangles
    
    // 3D dimensions
    private Double depth; // For 3D shapes
    private Double slantHeight; // For cones
}
