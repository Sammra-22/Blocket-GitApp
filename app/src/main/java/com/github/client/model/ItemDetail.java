package com.github.client.model;

import android.text.TextUtils;

/**
 * Created by Sam on 2/18/17.
 */
public class ItemDetail {

    String label;
    String value;

    public ItemDetail(String label, String value){
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public boolean isEmpty(){
        return TextUtils.isEmpty(value);
    }
}
