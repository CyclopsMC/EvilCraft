package org.cyclops.evilcraft.core.algorithm;

/**
 * Simple generic wrapper class.
 * @author rubensworks
 */
public class Wrapper<T> {

    private T value;

    public Wrapper() {
    }

    public Wrapper(T value) {
        set(value);
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

}
