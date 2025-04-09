package com.rslakra.distributedstore.lru;

public interface Store<K, V> {
    
    /**
     * @param key
     * @param value
     */
    void set(K key, V value);
    
    /**
     * @param key
     * @return
     */
    V get(K key);
    
    /**
     * @param key
     */
    void delete(K key);
    
}
