package com.rslakra.ratelimiter.fixedwindowcounter.service;

import com.rslakra.ratelimiter.fixedwindowcounter.payload.request.ShapeRequest;
import com.rslakra.ratelimiter.fixedwindowcounter.payload.response.ShapeResponse;
import com.rslakra.ratelimiter.fixedwindowcounter.persistence.model.Dimension;
import com.rslakra.ratelimiter.fixedwindowcounter.persistence.model.Shape;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for calculating geometric shape properties.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:10 PM
 */
@Service
public class ShapeService {

    /**
     * Calculates perimeter/area for 2D shapes or volume/surface area for 3D shapes.
     * Uses Shape model's perimeter calculation which supports both angle and dimension-based calculations.
     * 
     * @param shapeRequest the shape request
     * @return shape response with calculated properties
     */
    public ShapeResponse calculate(ShapeRequest shapeRequest) {
        if (Objects.isNull(shapeRequest.getShape())) {
            throw new IllegalArgumentException("The shape type should be provided!");
        }

        ShapeResponse shapeResponse = new ShapeResponse();
        shapeResponse.setShape(shapeRequest.getShape());
        Dimension dimension = shapeRequest.getDimension();
        Integer angle = shapeRequest.getAngle();

        // Use Shape model's perimeter calculation for 2D shapes (supports angle and dimensions)
        Shape shape = new Shape();
        shape.setShapeType(shapeRequest.getShape());
        shape.setDimension(dimension);
        shape.setAngle(angle);
        
        // Calculate perimeter using Shape model (supports angle or dimensions based on shape type)
        Double perimeter = shape.perimeter();
        shapeResponse.setPerimeter(perimeter);

        switch (shapeRequest.getShape()) {
            // 2D Shapes
            case CIRCLE:
                if (dimension != null && dimension.getRadius() != null) {
                    // Area = π * r^2
                    shapeResponse.setArea(Math.PI * dimension.getRadius() * dimension.getRadius());
                }
                break;

            case RECTANGLE:
                if (dimension == null || dimension.getLength() == null || dimension.getWidth() == null) {
                    throw new IllegalArgumentException("Rectangle requires dimension with length and width");
                }
                // Perimeter already calculated above using Shape model
                shapeResponse.setArea(calculateRectangleArea(dimension));
                break;

            case SQUARE:
                if (dimension != null && dimension.getSide() != null) {
                    // Area = side^2
                    shapeResponse.setArea(dimension.getSide() * dimension.getSide());
                }
                break;

            case TRIANGLE:
                if (dimension != null && dimension.getBase() != null && dimension.getHeight() != null) {
                    // Area = (base * height) / 2
                    shapeResponse.setArea((dimension.getBase() * dimension.getHeight()) / 2.0);
                } else if (dimension != null && dimension.getSide1() != null && dimension.getSide2() != null && dimension.getSide3() != null) {
                    // Heron's formula for area when all three sides are known
                    double s = (dimension.getSide1() + dimension.getSide2() + dimension.getSide3()) / 2.0;
                    shapeResponse.setArea(Math.sqrt(s * (s - dimension.getSide1()) * (s - dimension.getSide2()) * (s - dimension.getSide3())));
                }
                break;

            case PARALLELOGRAM:
                if (dimension != null && dimension.getBase() != null && dimension.getHeight() != null) {
                    // Area = base * height
                    shapeResponse.setArea(dimension.getBase() * dimension.getHeight());
                }
                break;

            case TRAPEZOID:
                if (dimension != null && dimension.getBase() != null && dimension.getLength() != null && dimension.getHeight() != null) {
                    // Area = ((base1 + base2) * height) / 2
                    shapeResponse.setArea(((dimension.getBase() + dimension.getLength()) * dimension.getHeight()) / 2.0);
                }
                break;

            case POLYGON:
                // Perimeter already calculated above using Shape model (supports angle-based calculation)
                // Area calculation for regular polygon: (n * s^2) / (4 * tan(π/n))
                if (dimension != null && dimension.getSide() != null && angle != null && angle > 0 && angle < 180) {
                    int numberOfSides = (int) Math.round(360.0 / (180.0 - angle));
                    if (numberOfSides > 2 && numberOfSides <= 100) {
                        double sideLength = dimension.getSide();
                        double area = (numberOfSides * sideLength * sideLength) / (4.0 * Math.tan(Math.PI / numberOfSides));
                        shapeResponse.setArea(area);
                    }
                }
                break;

            case HEXAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Area of regular hexagon = (3 * sqrt(3) * side^2) / 2
                    shapeResponse.setArea((3 * Math.sqrt(3) * dimension.getSide() * dimension.getSide()) / 2.0);
                }
                break;

            case OCTAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Area of regular octagon = 2 * (1 + sqrt(2)) * side^2
                    shapeResponse.setArea(2 * (1 + Math.sqrt(2)) * dimension.getSide() * dimension.getSide());
                }
                break;

            case HEPTAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Area of regular heptagon = (7/4) * side^2 * cot(π/7)
                    shapeResponse.setArea((7.0 / 4.0) * dimension.getSide() * dimension.getSide() * (1.0 / Math.tan(Math.PI / 7.0)));
                }
                break;

            // 3D Shapes
            case SPHERE:
                if (dimension != null && dimension.getRadius() != null) {
                    // Volume = (4/3) * π * r^3
                    shapeResponse.setVolume((4.0 / 3.0) * Math.PI * dimension.getRadius() * dimension.getRadius() * dimension.getRadius());
                    // Surface Area = 4 * π * r^2
                    shapeResponse.setSurfaceArea(4 * Math.PI * dimension.getRadius() * dimension.getRadius());
                }
                break;

            case CUBE:
                if (dimension != null && dimension.getSide() != null) {
                    // Volume = side^3
                    shapeResponse.setVolume(dimension.getSide() * dimension.getSide() * dimension.getSide());
                    // Surface Area = 6 * side^2
                    shapeResponse.setSurfaceArea(6 * dimension.getSide() * dimension.getSide());
                }
                break;

            case CYLINDER:
                if (dimension != null && dimension.getRadius() != null && dimension.getHeight() != null) {
                    // Volume = π * r^2 * h
                    shapeResponse.setVolume(Math.PI * dimension.getRadius() * dimension.getRadius() * dimension.getHeight());
                    // Surface Area = 2 * π * r * (r + h)
                    shapeResponse.setSurfaceArea(2 * Math.PI * dimension.getRadius() * (dimension.getRadius() + dimension.getHeight()));
                }
                break;

            case CONE:
                if (dimension != null && dimension.getRadius() != null && dimension.getHeight() != null) {
                    // Volume = (1/3) * π * r^2 * h
                    shapeResponse.setVolume((1.0 / 3.0) * Math.PI * dimension.getRadius() * dimension.getRadius() * dimension.getHeight());
                    // Surface Area = π * r * (r + l) where l is slant height
                    if (dimension.getSlantHeight() != null) {
                        shapeResponse.setSurfaceArea(Math.PI * dimension.getRadius() * (dimension.getRadius() + dimension.getSlantHeight()));
                    } else {
                        // Calculate slant height: l = sqrt(r^2 + h^2)
                        double slantHeight = Math.sqrt(dimension.getRadius() * dimension.getRadius() + dimension.getHeight() * dimension.getHeight());
                        shapeResponse.setSurfaceArea(Math.PI * dimension.getRadius() * (dimension.getRadius() + slantHeight));
                    }
                }
                break;

            case CUBOID:
                if (dimension != null && dimension.getLength() != null && dimension.getWidth() != null && dimension.getHeight() != null) {
                    // Volume = length * width * height
                    shapeResponse.setVolume(dimension.getLength() * dimension.getWidth() * dimension.getHeight());
                    // Surface Area = 2 * (lw + lh + wh)
                    shapeResponse.setSurfaceArea(2 * (dimension.getLength() * dimension.getWidth() + 
                        dimension.getLength() * dimension.getHeight() + 
                        dimension.getWidth() * dimension.getHeight()));
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported shape type: " + shapeRequest.getShape());
        }

        return shapeResponse;
    }

    /**
     * Legacy method for backward compatibility.
     * 
     * @param shapeRequest the shape request
     * @return shape response
     */
    public ShapeResponse perimeter(ShapeRequest shapeRequest) {
        return calculate(shapeRequest);
    }

    /**
     * Helper method to calculate rectangle area.
     * 
     * @param dimension the dimension
     * @return area
     */
    private Double calculateRectangleArea(Dimension dimension) {
        if (dimension == null || dimension.getLength() == null || dimension.getWidth() == null) {
            return null;
        }
        return dimension.getLength() * dimension.getWidth();
    }
}
