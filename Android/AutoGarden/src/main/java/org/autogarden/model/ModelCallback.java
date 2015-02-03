package org.autogarden.model;

public interface ModelCallback<T> {

    void success(T data);

    void fail();
}