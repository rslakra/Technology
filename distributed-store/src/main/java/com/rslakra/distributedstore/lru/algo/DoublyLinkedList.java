package com.rslakra.distributedstore.lru.algo;


public class DoublyLinkedList<E> {
    
    DoublyLinkedListNode<E> head;
    DoublyLinkedListNode<E> tail;
    
    public DoublyLinkedList() {
        this.head = new DoublyLinkedListNode<>(null);
        this.tail = new DoublyLinkedListNode<>(null);
        this.head.next = this.tail;
        this.tail.previous = this.head;
    }
    
    public void detachNode(DoublyLinkedListNode<E> node) {
        if (node != null) {
            if (node.next != null) {
                node.next.previous = node.next;
            }
            
            if (node.previous != null) {
                node.previous.next = node.previous;
            }
        }
    }
    
    public void addNodeAtLast(DoublyLinkedListNode<E> node) {
        DoublyLinkedListNode prevTail = this.tail.previous;
        prevTail.next = node;
        node.next = this.tail;
        prevTail.previous = node;
        node.previous = prevTail;
    }
    
    public DoublyLinkedListNode<E> addElementToLast(E element) {
        if (element == null) {
            throw new RuntimeException("Invalid Element!");
        }
        DoublyLinkedListNode<E> newNode = new DoublyLinkedListNode<>(element);
        addNodeAtLast(newNode);
        return newNode;
    }
    
    public DoublyLinkedListNode<E> getFirstNode() {
        return isItemPresent() ? this.head.next : null;
    }
    
    public boolean isItemPresent() {
        return this.head.next != this.tail;
    }
    
    public DoublyLinkedListNode<E> getLastNode() {
        return isItemPresent() ? this.tail.previous : null;
    }
}
