package com.rslakra.distributedstore.lru;

public interface EvictionPolicy<E> {
    
    /**
     * @param key
     */
    void accessedKey(E key);
    
    /**
     * @return
     */
    E evict();
}
