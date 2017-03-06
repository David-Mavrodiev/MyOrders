package com.topoffers.data.services;

import com.google.gson.Gson;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IHaveImageFile;
import com.topoffers.data.base.RequestWithBodyType;
import com.topoffers.data.models.Header;
import com.topoffers.data.models.Headers;
import com.topoffers.data.models.QueryParam;
import com.topoffers.data.models.QueryParams;
import com.topoffers.data.models.RequestFormBodyKeys;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRestData<T> implements IData<T> {
    private final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/png");

    private final String url;
    private final Class<T> klassSingle;
    private final Class<T[]> klassArray;
    private final OkHttpClient httpClient;

    public HttpRestData(String url, Class<T> klassSingle, Class<T[]> klassArray) {
        this.url = url;
        this.klassSingle = klassSingle;
        this.klassArray = klassArray;

        this.httpClient = new OkHttpClient();
    }

    public Observable<T[]> getAll(final Headers headers) {
        return Observable
            .create(new ObservableOnSubscribe<T[]>() {
                @Override
                public void subscribe(ObservableEmitter<T[]> e) throws Exception {
                    Request request = buildGetRequest(url, headers);
                    Response response = httpClient.newCall(request).execute();

                    T[] objects = parseArray(response.body().string());
                    e.onNext(objects);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<T> getSingle(final Headers headers) {
        return Observable
                .create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> e) throws Exception {
                        Request request = buildGetRequest(url, headers);
                        Response response = httpClient.newCall(request).execute();

                        T object = parseSingle(response.body().string());
                        e.onNext(object);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<T[]> getAllWithQueryParams(final QueryParams queryParams, final Headers headers) {
        return Observable
            .create(new ObservableOnSubscribe<T[]>() {
                @Override
                public void subscribe(ObservableEmitter<T[]> e) throws Exception {
                    String urlWithQueries = buildUrlWithQueryParams(url ,queryParams);
                    Request request = buildGetRequest(urlWithQueries, headers);
                    Response response = httpClient.newCall(request).execute();

                    T[] objects = parseArray(response.body().string());
                    e.onNext(objects);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> getById(final Object id, final Headers headers) { // id can be string or integer
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildGetRequest(url + "/" + id, headers);
                Response response = httpClient.newCall(request).execute();

                T object = parseSingle(response.body().string());
                e.onNext(object);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> add(final T object, final Headers headers) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildRequestWithBody(object, url, headers);
                Response response = httpClient.newCall(request).execute();

                T resultObject = parseSingle(response.body().string());
                e.onNext(resultObject);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> edit(final T object, final Headers headers) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildRequestWithBody(RequestWithBodyType.PUT, object, url, headers);
                Response response = httpClient.newCall(request).execute();

                T resultObject = parseSingle(response.body().string());
                e.onNext(resultObject);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> edit(final Object id, final T object, final Headers headers) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildRequestWithBody(RequestWithBodyType.PUT, object, url + "/" + id, headers);
                Response response = httpClient.newCall(request).execute();

                T resultObject = parseSingle(response.body().string());
                e.onNext(resultObject);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> delete(final Object id, final Headers headers) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildDeleteRequest(url + "/" + id, headers);
                Response response = httpClient.newCall(request).execute();

                T resultObject = parseSingle(response.body().string());
                e.onNext(resultObject);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> custom(final RequestWithBodyType requestType, final Object object) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Request request = buildCustomRequestWithBody(requestType, object, url);
                Response response = httpClient.newCall(request).execute();

                T resultObject = parseSingle(response.body().string());
                e.onNext(resultObject);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> addMultipartWithImage(final IHaveImageFile object, final RequestFormBodyKeys requestFormBodyKeys, final Headers headers) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                RequestBody requestBody = buildRequestFormBody(requestFormBodyKeys, object);
                Request request = buildMultipartRequest(RequestWithBodyType.POST, url, requestBody, headers);

                try {
                    Response response = httpClient.newCall(request).execute();

                    T responseObject = parseSingle(response.body().string());
                    e.onNext(responseObject);
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private String buildUrlWithQueryParams(String url, QueryParams queryParams) {
        StringBuilder urlWithQueries = new StringBuilder();
        urlWithQueries.append(url);
        if (queryParams.getQueryParams().size() > 0) {
            urlWithQueries.append("?");
        }
        for (QueryParam queryParam : queryParams.getQueryParams()) {
            urlWithQueries.append(queryParam.getKey());
            urlWithQueries.append("=");
            urlWithQueries.append(queryParam.getValue());
            urlWithQueries.append("&");
        }

        return urlWithQueries.toString();
    }

    private Request buildGetRequest(String url) {
        return  buildGetRequest(url, new Headers(new ArrayList<Header>()));
    }

    private Request buildGetRequest(String url, Headers headers) {
        Request.Builder requestBuilder = new Request.Builder()
            .url(url);
        requestBuilder = this.addHeadersToRequestBuilder(requestBuilder, headers);
        return requestBuilder.build();
    }

    private Request buildDeleteRequest(String url, Headers headers) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        requestBuilder = requestBuilder.delete();
        requestBuilder = this.addHeadersToRequestBuilder(requestBuilder, headers);
        return requestBuilder.build();
    }

    private Request.Builder addHeadersToRequestBuilder(Request.Builder requestBuilder, Headers headers) {
        for (Header header : headers.getHeaders()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        return requestBuilder;
    }

    private Request buildRequestWithBody(T object, String url) {
        return this.buildRequestWithBody(RequestWithBodyType.POST, object, url, new Headers(new ArrayList<Header>()));
    }

    private Request buildRequestWithBody(T object, String url, Headers headers) {
        return this.buildRequestWithBody(RequestWithBodyType.POST, object, url, headers);
    }

    private Request buildRequestWithBody(RequestWithBodyType type, T object, String url, Headers headers) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(object);
        return buildRequestWithBody(type, jsonBody, url, headers);

    }

    private Request buildCustomRequestWithBody(RequestWithBodyType type, Object object, String url) {
        return buildCustomRequestWithBody(type, object, url, new Headers(new ArrayList<Header>()));
    }

    private Request buildCustomRequestWithBody(RequestWithBodyType type, Object object, String url, Headers headers) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(object);
        return buildRequestWithBody(type, jsonBody, url, headers);
    }

    private Request buildRequestWithBody(RequestWithBodyType type, String jsonBody, String url, Headers headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        switch (type){
            case POST:
                requestBuilder = requestBuilder.post(requestBody);
                break;
            case PUT:
                requestBuilder = requestBuilder.put(requestBody);
                break;
        }

        for (Header header : headers.getHeaders()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }

        Request request = requestBuilder.build();
        return request;
    }

    private RequestBody buildRequestFormBody(RequestFormBodyKeys requestFormBodyKeys, IHaveImageFile object) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png", RequestBody.create(MEDIA_TYPE_IMAGE, object.getImageFile()));

        for (String key : requestFormBodyKeys.getKeys()) {
            String methodName = String.format("get%s", Character.toUpperCase(key.charAt(0)) + key.substring(1));
            try {
                Method method = object.getClass().getMethod(methodName);
                Object value = method.invoke(object);
                requestBodyBuilder.addFormDataPart(key, value.toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        RequestBody requestBody = requestBodyBuilder.build();
        return requestBody;
    }

    private Request buildMultipartRequest(RequestWithBodyType type, String url, RequestBody requestBody, Headers headers) {
        Request.Builder requestBuilder = new Request.Builder()
            .url(url);

        switch (type) {
            case POST:
                requestBuilder = requestBuilder.post(requestBody);
        }

        for (Header header : headers.getHeaders()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }

        return requestBuilder.build();
    }

    private T[] parseArray(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, this.klassArray);
    }

    private T parseSingle(String string) {
        Gson gson = new Gson();
        return  gson.fromJson(string, this.klassSingle);
    }
}
