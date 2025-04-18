package com.rslakra.distributedstore.lru;

public class Cache<K, V> {
    
    private final EvictionPolicy<K> evictionPolicy;
    private final Store<K, V> store;
    
    public Cache(EvictionPolicy<K> evictionPolicy, Store<K, V> store) {
        this.evictionPolicy = evictionPolicy;
        this.store = store;
    }
    
    public V get(K key) {
        try {
            V value = this.store.get(key);
            this.evictionPolicy.accessedKey(key);
            return value;
        } catch (KeyNotFoundException e) {
            System.out.println("No Key Found!");
            return null;
        }
    }
    
    public void set(K key, V value) {
        try {
            this.store.set(key, value);
            this.evictionPolicy.accessedKey(key);
        } catch (StorageFullException ex) {
            K keyToRemove = this.evictionPolicy.evict();
            if (keyToRemove == null) {
                throw new RuntimeException("Unexpected State. Storage full and no key to evict.");
            }
            this.store.delete(keyToRemove);
            this.set(key, value);
        }
    }
}
