package com.topoffers.data.base;

import com.topoffers.data.models.Headers;
import com.topoffers.data.models.QueryParams;
import com.topoffers.data.models.RequestFormBodyKeys;

import io.reactivex.Observable;

public interface IData<T> {
    Observable<T[]> getAll(Headers headers);
    Observable<T> getSingle(Headers headers);
    Observable<T[]> getAllWithQueryParams(QueryParams queryparams, Headers headers);
    Observable<T> getById(Object id, Headers headers); // id can be string or integer
    Observable<T> add(T object, Headers headers);
    Observable<T> edit(T object, Headers headers);
    Observable<T> edit(Object id, T object, Headers headers);
    Observable<T> delete(Object id, Headers headers);
    Observable<T> custom(RequestWithBodyType requestType, Object object);
    Observable<T> addMultipartWithImage(IHaveImageFile object, RequestFormBodyKeys requestFormBodyKeys, Headers headers);
}