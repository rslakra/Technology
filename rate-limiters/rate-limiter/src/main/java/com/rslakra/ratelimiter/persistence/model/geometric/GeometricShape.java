package com.rslakra.ratelimiter.persistence.model.geometric;

import com.rslakra.ratelimiter.persistence.model.Shape;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rohtash Lakra
 * @created 4/14/23 3:21 PM
 */
@Data
@EqualsAndHashCode
public class GeometricShape extends Shape {

    private int angle;

}
