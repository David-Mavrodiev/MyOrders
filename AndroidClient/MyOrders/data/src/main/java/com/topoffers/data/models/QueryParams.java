package com.topoffers.data.models;

import java.util.List;

/**
 * Created by Simeon on 1.3.2017 г..
 */

public class QueryParams {
    private List<QueryParam> queryParams;

    public QueryParams(List<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }
}
