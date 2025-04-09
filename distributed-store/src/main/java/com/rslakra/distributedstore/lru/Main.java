package com.rslakra.distributedstore.lru;


import com.rslakra.distributedstore.lru.policy.LRUEviction;
import com.rslakra.distributedstore.lru.store.HashMapStore;

public class Main {

    public static void main(String[] args) {
        LRUEviction lruEviction = new LRUEviction<>();
        Store store = new HashMapStore(100);
        Cache<String, String> cache = new Cache<>(lruEviction, store);
        cache.set("key1", "value1");
        cache.set("key2", "value2");
        cache.set("key2", "value2.1");
        System.out.println(cache.get("key2"));
    }
}
