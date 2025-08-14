package com.rslakra.distributedstore.lru.store;

import com.rslakra.distributedstore.lru.KeyNotFoundException;
import com.rslakra.distributedstore.lru.StorageFullException;
import com.rslakra.distributedstore.lru.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapStore<K, V> implements Store<K, V> {

    private Map<K, V> dataStore;
    private final int capacity;

    /**
     * @param capacity
     */
    public HashMapStore(int capacity) {
        this.dataStore = new ConcurrentHashMap<>();
        this.capacity = capacity;
    }

    @Override
    public void set(K key, V value) {
        if (this.isStorageFull()) {
            throw new StorageFullException("Capacity is full!");
        }
        this.dataStore.put(key, value);
    }

    /**
     * Checks if the key exists or not and throws an error if not exists.
     *
     * @param key
     */
    private void checkKeyExists(K key) {
        if (!isKeyExist(key)) {
            throw new KeyNotFoundException("No Key Found!");
        }
    }

    @Override
    public V get(K key) {
        checkKeyExists(key);
        return this.dataStore.get(key);
    }

    @Override
    public void delete(K key) {
        checkKeyExists(key);
        this.dataStore.remove(key);
    }

    private boolean isStorageFull() {
        return this.dataStore.size() >= this.capacity;
    }

    private boolean isKeyExist(K key) {
        return this.dataStore.containsKey(key);
    }
}
