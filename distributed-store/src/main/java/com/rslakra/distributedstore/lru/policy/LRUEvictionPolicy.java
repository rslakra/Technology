package com.rslakra.distributedstore.lru.policy;


import com.rslakra.distributedstore.lru.EvictionPolicy;
import com.rslakra.distributedstore.lru.algo.DoublyLinkedList;
import com.rslakra.distributedstore.lru.algo.DoublyLinkedListNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    
    private final DoublyLinkedList<K> linkedList;
    private final Map<K, DoublyLinkedListNode<K>> mapper;
    
    public LRUEvictionPolicy() {
        this.linkedList = new DoublyLinkedList<>();
        this.mapper = new ConcurrentHashMap<>();
    }
    
    @Override
    public void accessedKey(K key) {
        if (this.mapper.containsKey(key)) {
            linkedList.detachNode(this.mapper.get(key));
            linkedList.addNodeAtLast(this.mapper.get(key));
        } else {
            DoublyLinkedListNode<K> newNode = new DoublyLinkedListNode<>(key);
            this.mapper.put(key, newNode);
        }
    }
    
    @Override
    public K evict() {
        DoublyLinkedListNode<K> nodeToRemove = this.linkedList.getFirstNode();
        if (nodeToRemove == null) {
            return null;
        }
        
        linkedList.detachNode(nodeToRemove);
        return nodeToRemove.getElement();
    }
}
