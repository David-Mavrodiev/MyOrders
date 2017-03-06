package com.topoffers.data.models;

/**
 * Created by Simeon on 1.3.2017 г..
 */

public class QueryParam {
    private String key;
    private String value;

    public QueryParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
