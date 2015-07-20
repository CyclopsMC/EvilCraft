package org.cyclops.evilcraft.core.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * A stack with limited size that automatically removes elements at the bottom of the stack if required.
 * takeLast
 * @author rubensworks
 */
public class EvictingStack<T> {

    private int size;
    private ArrayList<T> stack;
    private int top;
    private int popBalance = 0;

    public EvictingStack(int size) {
        this.size = size;
        this.stack = Lists.newArrayListWithCapacity(size);
        this.top = 0;
    }

    public void push(T element) {
        if(top == maxSize()) top = 0;
        stack.add(top, element);
        top++;
        if(popBalance < size - 1) popBalance++;
    }

    public T pop() {
        if(top - 1 < 0) top = size;
        T element = stack.get(--top);
        popBalance--;
        return element;
    }

    public int maxSize() {
        return size;
    }

    public int currentSize() {
        return popBalance;
    }

}
