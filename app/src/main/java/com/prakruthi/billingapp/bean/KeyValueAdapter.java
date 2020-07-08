package com.prakruthi.billingapp.bean;

/**
 * Created by Admin on 1/21/2017.
 */

public class KeyValueAdapter {

    public String string;
    public Object tag;

    public KeyValueAdapter(String string, Object tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}
