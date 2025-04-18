package com.rslakra.distributedstore.ds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This KeyValueStore class implements optimistic concurrency control:
 * <p>
 * It allows multiple transactions to proceed without blocking.
 * Conflicts are detected at commit time by checking if any read values have changed.
 * Write operations are buffered in the transaction and only applied if the commit is successful.
 * It provides ACID properties (Atomicity, Consistency, Isolation, Durability) for transactions.
 * <p>
 * An in-memory transaction management system (Data Plane)
 * A distributed storage system (Data Plane)
 * A built-in distributed configuration system (Control Plane).
 */
public class KeyValueStore implements Cache {
    
    /**
     * The main storage for key-value pairs.
     * Each value is wrapped in a VersionedValue object, which includes the value and its version.
     */
    private Map<String, VersionedValue> dataStore = new HashMap<>();
    
    /**
     * Allows each thread to have its own transaction context.
     * Ensures thread safety for concurrent transactions.
     */
    private ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();
    
    /**
     * A global counter for generating unique version numbers.
     * AtomicLong ensures thread-safe increments.
     */
    private AtomicLong globalVersion = new AtomicLong(0);
    
    // Methods: get, set, delete, begin, commit, rollback
    
    /**
     * Check if there's an active transaction.
     * If so, check the transaction's deleteSet and writeSet first.
     * If not found in the transaction, reads from the main data store.
     * Records the read in the transaction's readSet for later conflict detection.
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        // Check if there's an active transaction.
        Transaction transaction = currentTransaction.get();
        if (transaction != null) {
            if (transaction.getDeleteSet().contains(key)) {
                return null;
            }
            if (transaction.getWriteSet().containsKey(key)) {
                return transaction.getWriteSet().get(key);
            }
        }
        
        VersionedValue versionedValue = dataStore.get(key);
        if (versionedValue == null) {
            return null;
        }
        
        if (transaction != null) {
            transaction.getReadSet().putIfAbsent(key, versionedValue);
        }
        
        return versionedValue.getValue();
    }
    
    /**
     * If not in a transaction, directly updates the main data store with a new version.
     * If in a transaction, adds to the writeSet and removes from deleteSet.
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        Transaction transaction = currentTransaction.get();
        // If not in a transaction, directly updates the main data store with a new version.
        if (transaction == null) {
            dataStore.put(key, new VersionedValue(value, globalVersion.incrementAndGet()));
        } else {
            transaction.getWriteSet().put(key, value);
            transaction.getDeleteSet().remove(key);
        }
    }
    
    /**
     * If not in a transaction, directly removed from the main data store.
     * If in a transaction, adds to deleteSet and removes from writeSet.
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        Transaction transaction = currentTransaction.get();
        if (transaction == null) {
            dataStore.remove(key);
        } else {
            transaction.getDeleteSet().add(key);
            transaction.getWriteSet().remove(key);
        }
    }
    
    /**
     * Starts a new transaction if one isn’t already in progress.
     */
    @Override
    public void begin() {
        if (currentTransaction.get() != null) {
            throw new IllegalStateException("Transaction already in progress!");
        }
        
        currentTransaction.set(new Transaction());
    }
    
    /**
     * Checks for conflicts by comparing versions in the readSet with current versions.
     * If no conflicts, applies changes from deleteSet and writeSet to the main data store.
     * Returns true if the commit was successful, false if there was a conflict.
     *
     * @return
     */
    @Override
    public boolean commit() {
        Transaction transaction = currentTransaction.get();
        if (transaction == null) {
            throw new IllegalStateException("No active transaction!");
        }
        
        // Validate read set
        for (Map.Entry<String, VersionedValue> entry : transaction.getReadSet().entrySet()) {
            VersionedValue versionedValue = dataStore.get(entry.getKey());
            if (versionedValue == null || versionedValue.getVersion() != entry.getValue().getVersion()) {
                // Conflict detected
                currentTransaction.remove();
                return false;
            }
        }
        
        // Apply changes
        for (String key : transaction.getDeleteSet()) {
            dataStore.remove(key);
        }
        
        for (Map.Entry<String, String> entry : transaction.getWriteSet().entrySet()) {
            dataStore.put(entry.getKey(), new VersionedValue(entry.getValue(), globalVersion.incrementAndGet()));
        }
        
        currentTransaction.remove();
        return true;
    }
    
    /**
     * Discards the current transaction without applying any changes.
     */
    @Override
    public void rollback() {
        Transaction transaction = currentTransaction.get();
        if (transaction == null) {
            throw new IllegalStateException("No active transaction!");
        }
        
        currentTransaction.remove();
    }
}
