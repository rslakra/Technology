package com.rslakra.distributedratelimiter.payload.request;

import com.rslakra.distributedratelimiter.enums.ShapeType;
import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import lombok.Data;

/**
 * @author Rohtash Lakra
 * @created 4/14/23 3:45 PM
 */
@Data
public class ShapeRequest {

    private ShapeType shape;
    private Dimension dimension;

}
