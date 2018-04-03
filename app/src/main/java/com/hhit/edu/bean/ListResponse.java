package com.hhit.edu.bean;

import java.util.List;

/**
 * Created by 93681 on 2018/4/1.
 * 可以接受任何对象的数组
 */

public class ListResponse<T> extends Response {
    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
