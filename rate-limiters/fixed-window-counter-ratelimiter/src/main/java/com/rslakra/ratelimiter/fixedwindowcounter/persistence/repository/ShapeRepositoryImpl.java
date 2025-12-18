package com.rslakra.ratelimiter.fixedwindowcounter.persistence.repository;

import com.rslakra.ratelimiter.fixedwindowcounter.enums.ShapeType;
import com.rslakra.ratelimiter.fixedwindowcounter.payload.response.ShapeResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ShapeRepository.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Repository
public class ShapeRepositoryImpl implements ShapeRepository {

    private final Map<String, ShapeResponse> storage = new ConcurrentHashMap<>();
    private final Map<String, String> idToShapeType = new ConcurrentHashMap<>();

    @Override
    public ShapeResponse save(ShapeResponse shapeResponse) {
        if (shapeResponse == null) {
            throw new IllegalArgumentException("Shape response cannot be null");
        }
        
        if (shapeResponse.getShape() == null) {
            throw new IllegalArgumentException("Shape type cannot be null");
        }
        
        String id = UUID.randomUUID().toString();
        // Store the shape response with generated ID
        storage.put(id, shapeResponse);
        idToShapeType.put(id, shapeResponse.getShape().name());
        
        return shapeResponse;
    }

    @Override
    public Optional<ShapeResponse> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<ShapeResponse> findByShapeType(ShapeType shapeType) {
        if (shapeType == null) {
            return new ArrayList<>(storage.values());
        }
        return storage.entrySet().stream()
            .filter(entry -> idToShapeType.get(entry.getKey()).equals(shapeType.name()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public List<ShapeResponse> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(String id) {
        idToShapeType.remove(id);
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
        idToShapeType.clear();
    }

    @Override
    public long count() {
        return storage.size();
    }
}

