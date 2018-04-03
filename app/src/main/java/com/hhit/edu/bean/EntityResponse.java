package com.hhit.edu.bean;

/**
 * Created by 93681 on 2018/4/1.
 * 可以接受任何实体对象
 */

public class EntityResponse<T> extends Response{
    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
