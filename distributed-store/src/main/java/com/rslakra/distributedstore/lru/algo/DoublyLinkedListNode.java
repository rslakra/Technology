package com.rslakra.distributedstore.lru.algo;


import lombok.Getter;

@Getter
public class DoublyLinkedListNode<E> {
    
    E element;
    DoublyLinkedListNode<E> previous;
    DoublyLinkedListNode<E> next;

    /**
     * @param element
     */
    public DoublyLinkedListNode(E element) {
        this.element = element;
        this.previous = null;
        this.next = null;
    }
}
