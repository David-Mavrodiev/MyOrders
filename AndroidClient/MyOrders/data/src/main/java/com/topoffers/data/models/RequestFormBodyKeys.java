package com.topoffers.data.models;

import java.util.List;

/**
 * Created by Simeon on 28.2.2017 г..
 */

public class RequestFormBodyKeys {
    private List<String> keys;

    public RequestFormBodyKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getKeys() {
        return keys;
    }
}
