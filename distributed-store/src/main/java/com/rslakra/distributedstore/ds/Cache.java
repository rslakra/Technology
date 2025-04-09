package com.rslakra.distributedstore.ds;

public interface Cache {
    
    /**
     * Check if there's an active transaction.
     * If so, check the transaction's deleteSet and writeSet first.
     * If not found in the transaction, reads from the main data store.
     * Records the read in the transaction's readSet for later conflict detection.
     *
     * @param key
     * @return
     */
    String get(String key);
    
    /**
     * If in a transaction, adds to the writeSet and removes from deleteSet.
     * If not in a transaction, directly updates the main data store with a new version.
     *
     * @param key
     * @param value
     */
    void set(String key, String value);
    
    /**
     * If in a transaction, adds to deleteSet and removes from writeSet.
     * If not in a transaction, directly removed from the main data store.
     *
     * @param key
     */
    void delete(String key);
    
    /**
     * Starts a new transaction if one isn’t already in progress.
     */
    void begin();
    
    /**
     * Checks for conflicts by comparing versions in the readSet with current versions.
     * If no conflicts, applies changes from deleteSet and writeSet to the main data store.
     * Returns true if the commit was successful, false if there was a conflict.
     *
     * @return
     */
    boolean commit();
    
    /**
     * Discards the current transaction without applying any changes.
     */
    void rollback();
}
