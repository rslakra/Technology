package com.rslakra.ratelimiter.slidingwindowcounter.persistence.model;

import com.rslakra.ratelimiter.slidingwindowcounter.enums.ShapeType;
import lombok.Data;

/**
 * Base class for all geometric shapes.
 * 
 * @author Rohtash Lakra
 * @created 4/14/23 3:20 PM
 */
@Data
public class Shape {

    /**
     * Angle of the shape (in degrees).
     */
    private Integer angle;

    /**
     * Dimension of the shape.
     */
    private Dimension dimension;

    /**
     * Shape type.
     */
    private ShapeType shapeType;

    /**
     * Calculates the perimeter of the shape based on shape type, angle, or dimensions.
     * 
     * @return perimeter, or null if not applicable or insufficient data
     */
    public Double perimeter() {
        if (shapeType == null) {
            return null;
        }

        return calculatePerimeter(shapeType, angle, dimension);
    }

    /**
     * Calculates perimeter based on shape type, using angle or dimensions as appropriate.
     * 
     * @param shapeType the type of shape
     * @param angle the angle in degrees (optional, used for some shapes)
     * @param dimension the dimensions (optional, used for most shapes)
     * @return calculated perimeter, or null if insufficient data
     */
    public static Double calculatePerimeter(ShapeType shapeType, Integer angle, Dimension dimension) {
        if (shapeType == null) {
            return null;
        }

        switch (shapeType) {
            case CIRCLE:
                if (dimension != null && dimension.getRadius() != null) {
                    // Perimeter (circumference) = 2 * π * r
                    return 2 * Math.PI * dimension.getRadius();
                }
                break;

            case SQUARE:
                if (dimension != null && dimension.getSide() != null) {
                    // Perimeter = 4 * side
                    return 4 * dimension.getSide();
                }
                break;

            case RECTANGLE:
                if (dimension != null && dimension.getLength() != null && dimension.getWidth() != null) {
                    // Perimeter = 2 * (length + width)
                    return 2 * (dimension.getLength() + dimension.getWidth());
                }
                break;

            case TRIANGLE:
                if (dimension != null) {
                    // If all three sides are provided
                    if (dimension.getSide1() != null && dimension.getSide2() != null && dimension.getSide3() != null) {
                        return dimension.getSide1() + dimension.getSide2() + dimension.getSide3();
                    }
                    // If base and two sides are provided
                    if (dimension.getBase() != null && dimension.getSide1() != null && dimension.getSide2() != null) {
                        return dimension.getBase() + dimension.getSide1() + dimension.getSide2();
                    }
                }
                break;

            case PARALLELOGRAM:
                if (dimension != null) {
                    // If length and width are provided
                    if (dimension.getLength() != null && dimension.getWidth() != null) {
                        return 2 * (dimension.getLength() + dimension.getWidth());
                    }
                    // If base and side are provided
                    if (dimension.getBase() != null && dimension.getSide() != null) {
                        return 2 * (dimension.getBase() + dimension.getSide());
                    }
                }
                break;

            case TRAPEZOID:
                if (dimension != null) {
                    // Perimeter = base1 + base2 + side1 + side2
                    Double base1 = dimension.getBase();
                    Double base2 = dimension.getLength(); // Using length as base2
                    Double side1 = dimension.getSide1();
                    Double side2 = dimension.getSide2();
                    
                    if (base1 != null && base2 != null && side1 != null && side2 != null) {
                        return base1 + base2 + side1 + side2;
                    }
                }
                break;

            case POLYGON:
                if (dimension != null && dimension.getSide() != null) {
                    // For regular polygon: Perimeter = number of sides * side length
                    Double sideLength = dimension.getSide();
                    
                    if (angle != null && angle > 0 && angle < 180) {
                        // Calculate number of sides from interior angle for regular polygon
                        // For regular polygon: interior angle = (n-2) * 180 / n
                        // Solving for n: n = 360 / (180 - angle)
                        int numberOfSides = (int) Math.round(360.0 / (180.0 - angle));
                        
                        // Use side as side length, calculate perimeter
                        if (numberOfSides > 2 && numberOfSides <= 100 && sideLength > 0) {
                            return numberOfSides * sideLength;
                        }
                    }
                    // If angle not provided, fall back to dimension-based calculation
                    // (This would require number of sides to be provided via other means)
                }
                break;

            case HEXAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Regular hexagon: Perimeter = 6 * side
                    return 6 * dimension.getSide();
                } else if (angle != null) {
                    // For regular hexagon with angle, interior angle is 120 degrees
                    // Can calculate if we have other info
                }
                break;

            case OCTAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Regular octagon: Perimeter = 8 * side
                    return 8 * dimension.getSide();
                }
                break;

            case HEPTAGON:
                if (dimension != null && dimension.getSide() != null) {
                    // Regular heptagon: Perimeter = 7 * side
                    return 7 * dimension.getSide();
                }
                break;

            default:
                // For unsupported shapes or 3D shapes, perimeter is not applicable
                return null;
        }

        return null;
    }
}
