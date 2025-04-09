package com.rslakra.distributedstore.lru;

import java.util.concurrent.Future;

public interface DataSource<K, V> {
    
    /**
     * @param key
     * @return
     */
    public Future<V> get(K key);
    
    /**
     * @param key
     * @param value
     * @return
     */
    public Future<Void> persist(K key, V value);
}
