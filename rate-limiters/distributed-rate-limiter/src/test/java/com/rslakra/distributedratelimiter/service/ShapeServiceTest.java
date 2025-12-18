package com.rslakra.distributedratelimiter.service;

import com.rslakra.distributedratelimiter.enums.ShapeType;
import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.payload.response.ShapeResponse;
import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShapeService.
 * Tests shape calculation functionality.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class ShapeServiceTest {

    private ShapeService shapeService;

    @BeforeEach
    void setUp() {
        shapeService = new ShapeService();
    }

    @Test
    void testPerimeter_WithValidRectangle_ShouldCalculateCorrectly() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(10.0);
        dimension.setWidth(5.0);
        request.setDimension(dimension);

        ShapeResponse response = shapeService.perimeter(request);

        assertNotNull(response);
        assertEquals(ShapeType.RECTANGLE, response.getShape());
        assertEquals(30.0, response.getPerimeter(), 0.01);
    }

    @Test
    void testPerimeter_WithNullShape_ShouldThrowException() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(null);

        assertThrows(IllegalArgumentException.class, () -> {
            shapeService.perimeter(request);
        });
    }

    @Test
    void testPerimeter_WithNullDimension_ShouldThrowException() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(ShapeType.RECTANGLE);
        request.setDimension(null);

        assertThrows(IllegalArgumentException.class, () -> {
            shapeService.perimeter(request);
        });
    }

    @Test
    void testPerimeter_WithNullLength_ShouldThrowException() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(null);
        dimension.setWidth(5.0);
        request.setDimension(dimension);

        assertThrows(IllegalArgumentException.class, () -> {
            shapeService.perimeter(request);
        });
    }

    @Test
    void testPerimeter_WithNullWidth_ShouldThrowException() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(10.0);
        dimension.setWidth(null);
        request.setDimension(dimension);

        assertThrows(IllegalArgumentException.class, () -> {
            shapeService.perimeter(request);
        });
    }

    @Test
    void testPerimeter_WithDifferentRectangleDimensions() {
        ShapeRequest request = new ShapeRequest();
        request.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(20.0);
        dimension.setWidth(10.0);
        request.setDimension(dimension);

        ShapeResponse response = shapeService.perimeter(request);

        assertEquals(60.0, response.getPerimeter(), 0.01);
    }
}

