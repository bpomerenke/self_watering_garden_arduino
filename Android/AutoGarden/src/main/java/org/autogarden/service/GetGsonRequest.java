package org.autogarden.service;

import com.android.volley.Response;

public class GetGsonRequest<T> extends AbstractGsonRequest<T> {

    public GetGsonRequest(String url, Class responseClass, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, responseClass, successListener, errorListener);
    }
}
