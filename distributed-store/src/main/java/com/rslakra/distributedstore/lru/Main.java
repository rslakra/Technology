package com.rslakra.distributedstore.lru;


import com.rslakra.distributedstore.lru.policy.LRUEvictionPolicy;
import com.rslakra.distributedstore.lru.store.HashMapStore;

public class Main {

    public static void main(String[] args) {
        LRUEvictionPolicy lruEvictionPolicy = new LRUEvictionPolicy<>();
        Store store = new HashMapStore(100);
        Cache<String, String> cache = new Cache<>(lruEvictionPolicy, store);
        cache.set("key1", "value1");
        cache.set("key2", "value2");
        cache.set("key2", "value2.1");
        System.out.println(cache.get("key2"));
    }
}
