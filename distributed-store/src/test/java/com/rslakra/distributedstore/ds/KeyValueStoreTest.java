package com.rslakra.distributedstore.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KeyValueStore Tests
 */
public class KeyValueStoreTest {
    
    private KeyValueStore store;
    
    @BeforeEach
    void setUp() {
        store = new KeyValueStore();
    }
    
    @Test
    void testBasicOperations() {
        store.set("key1", "value1");
        assertEquals("value1", store.get("key1"));
        
        store.set("key1", "updatedValue");
        assertEquals("updatedValue", store.get("key1"));
        
        store.delete("key1");
        assertNull(store.get("key1"));
    }
    
    @Test
    void testTransactionCommit() {
        store.set("key1", "initialValue");
        
        store.begin();
        store.set("key1", "transactionValue");
        store.set("key2", "newValue");
        assertTrue(store.commit());
        
        assertEquals("transactionValue", store.get("key1"));
        assertEquals("newValue", store.get("key2"));
    }
    
    @Test
    void testTransactionRollback() {
        store.set("key1", "initialValue");
        
        store.begin();
        store.set("key1", "transactionValue");
        store.set("key2", "newValue");
        store.rollback();
        
        assertEquals("initialValue", store.get("key1"));
        assertNull(store.get("key2"));
    }
    
    @Test
    void testDeleteInTransaction() {
        store.set("key1", "value1");
        
        store.begin();
        store.delete("key1");
        assertNull(store.get("key1"));
        assertTrue(store.commit());
        
        assertNull(store.get("key1"));
    }
    
    @Test
    void testReadYourOwnWrites() {
        store.begin();
        store.set("key1", "value1");
        assertEquals("value1", store.get("key1"));
        assertTrue(store.commit());
    }
    
    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void testConcurrentTransactions() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    store.begin();
                    store.set("key" + index, "value" + index);
                    assertTrue(store.commit());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        for (int i = 0; i < threadCount; i++) {
            assertEquals("value" + i, store.get("key" + i));
        }
    }
}
