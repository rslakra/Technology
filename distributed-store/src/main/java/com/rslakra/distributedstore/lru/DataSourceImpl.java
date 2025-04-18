package com.rslakra.distributedstore.lru;

import java.util.concurrent.Future;

public class DataSourceImpl implements DataSource<String, String> {
    
    @Override
    public Future<String> get(String key) {
        return null;
    }
    
    @Override
    public Future<Void> persist(String key, String value) {
        return null;
    }
}
