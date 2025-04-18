package com.rslakra.distributedstore.ds;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Transaction class is a crucial component in our optimistic concurrency
 * control mechanism. It acts as a container for all the operations performed
 * within a single transaction.
 */
@NoArgsConstructor
@Getter
public class Transaction {
    /**
     * Purpose:Tracks all the key-value pairs that have been read during this
     * transaction
     * <p>
     * Usage: Used for conflict detection during commit. If any key in the
     * readSet has a different version in the main store at commit time, it
     * indicates a conflict.
     */
    private Map<String, VersionedValue> readSet = new HashMap<>();
    
    /**
     * Purpose: Buffers all write operations performed during this transaction.
     * <p>
     * Usage: Stores all modifications without immediately applying them to the
     * main store. These changes are only applied if the transaction is
     * successfully committed.
     */
    private Map<String, String> writeSet = new HashMap<>();
    
    /**
     * Purpose: Tracks all keys that are marked for deletion in this transaction.
     * <p>
     * Usage: Store keys to be deleted without immediately removing them from
     * the main store. These deletions are only applied if the transaction is
     * successfully committed.
     */
    private Set<String> deleteSet = new HashSet<>();
}
