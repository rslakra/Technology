package com.rslakra.distributedstore.lru.store;

import com.rslakra.distributedstore.lru.KeyNotFoundException;
import com.rslakra.distributedstore.lru.StorageFullException;
import com.rslakra.distributedstore.lru.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapStore<K, V> implements Store<K, V> {
    
    private Map<K, V> map;
    private final int capacity;
    
    /**
     * @param capacity
     */
    public HashMapStore(int capacity) {
        this.map = new ConcurrentHashMap<>();
        this.capacity = capacity;
    }
    
    @Override
    public void set(K key, V value) {
        if (this.isStorageFull()) {
            throw new StorageFullException("Capacity is full");
        }
        this.map.put(key, value);
    }
    
    @Override
    public V get(K key) {
        if (!isKeyExist(key)) {
            throw new KeyNotFoundException("No Key Found!");
        }
        return this.map.get(key);
    }
    
    @Override
    public void delete(K key) {
        if (!isKeyExist(key)) {
            throw new KeyNotFoundException("No Key Found!");
        }
        this.map.remove(key);
    }
    
    private boolean isStorageFull() {
        return this.map.size() >= this.capacity;
    }
    
    private boolean isKeyExist(K key) {
        return this.map.containsKey(key);
    }
}
