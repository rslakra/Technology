package com.rslakra.ratelimiter.leakybucket.service;

import com.rslakra.ratelimiter.leakybucket.payload.request.ShapeRequest;
import com.rslakra.ratelimiter.leakybucket.payload.response.ShapeResponse;
import com.rslakra.ratelimiter.leakybucket.persistence.model.Dimension;
import com.rslakra.ratelimiter.leakybucket.persistence.model.Shape;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for calculating geometric shape properties.
 * Uses Shape model's perimeter calculation which supports both angle and dimension-based calculations.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:10 PM
 */
@Service
public class ShapeService {

    /**
     * Calculates perimeter using Shape model which supports both angle and dimension-based calculations.
     * 
     * @param shapeRequest the shape request
     * @return shape response with calculated perimeter
     */
    public ShapeResponse perimeter(ShapeRequest shapeRequest) {
        if (Objects.isNull(shapeRequest.getShape())) {
            throw new IllegalArgumentException("The shape type should be provided!");
        }

        ShapeResponse shapeResponse = new ShapeResponse();
        shapeResponse.setShape(shapeRequest.getShape());
        Dimension dimension = shapeRequest.getDimension();
        Integer angle = shapeRequest.getAngle();

        // Use Shape model's perimeter calculation (supports angle and dimensions)
        Shape shape = new Shape();
        shape.setShapeType(shapeRequest.getShape());
        shape.setDimension(dimension);
        shape.setAngle(angle);
        
        // Calculate perimeter using Shape model (supports angle or dimensions based on shape type)
        Double perimeter = shape.perimeter();
        shapeResponse.setPerimeter(perimeter);

        return shapeResponse;
    }
}
