package com.rslakra.distributedratelimiter.service;

import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.payload.response.ShapeResponse;
import com.rslakra.distributedratelimiter.persistence.model.geometric.twodimensional.Rectangle;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 4/14/23 3:10 PM
 */
@Service
public class ShapeService {

    /**
     * @param shapeRequest
     * @return
     */
    public ShapeResponse perimeter(ShapeRequest shapeRequest) {
        if (Objects.isNull(shapeRequest.getShape())) {
            throw new IllegalArgumentException("The shape type should provide!");
        }

        ShapeResponse shapeResponse = new ShapeResponse();
        shapeResponse.setShape(shapeRequest.getShape());
        switch (shapeRequest.getShape()) {
            case ARC:
                break;

            case RECTANGLE:
                if (shapeRequest.getDimension() == null 
                        || shapeRequest.getDimension().getLength() == null 
                        || shapeRequest.getDimension().getWidth() == null) {
                    throw new IllegalArgumentException("Rectangle requires dimension with length and width");
                }
                Rectangle rectangle = new Rectangle();
                rectangle.setDimension(shapeRequest.getDimension());
                shapeResponse.setPerimeter(rectangle.perimeter());
                break;
        }

        return shapeResponse;
    }


}
