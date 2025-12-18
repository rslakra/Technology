package com.rslakra.ratelimiter.tokenbucket.payload.response;

import com.rslakra.ratelimiter.tokenbucket.enums.ShapeType;
import lombok.Data;

/**
 * @author Rohtash Lakra
 * @created 4/14/23 3:46 PM
 */
@Data
public class ShapeResponse {

    private ShapeType shape;
    private Double perimeter;
}
