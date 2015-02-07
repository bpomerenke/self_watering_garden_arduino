package org.autogarden.service;

import com.android.volley.Response;

public class PutGsonRequest<T> extends AbstractGsonRequest<T> {
    private final Object requestObj;

    public PutGsonRequest(String url, Object requestObj, Class responseClass, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(Method.PUT, url, responseClass, successListener, listener);
        this.requestObj = requestObj;
    }

    @Override
    public byte[] getBody() {
        return gson.toJson(requestObj).getBytes();
    }
}
