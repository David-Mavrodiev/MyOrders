package com.topoffers.data.models;

import java.util.List;

public class Headers {
    public List<Header> getHeaders() {
        return headers;
    }

    public Headers(List<Header> headers) {
        this.headers = headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    private List<Header> headers;
}
