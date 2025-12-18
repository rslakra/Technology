package com.rslakra.ratelimiter.slidingwindowlog.payload.response;

import com.rslakra.ratelimiter.slidingwindowlog.enums.ShapeType;
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
