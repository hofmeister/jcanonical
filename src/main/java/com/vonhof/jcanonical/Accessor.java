package com.vonhof.jcanonical;


public class Accessor<T> {
    private final T delegate;

    protected Accessor(T delegate) {
        this.delegate = delegate;
    }

    public T delegate() {
        return delegate;
    }
}
