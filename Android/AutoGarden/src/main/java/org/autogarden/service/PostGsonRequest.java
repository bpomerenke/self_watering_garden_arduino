package org.autogarden.service;

import com.android.volley.Response;

public class PostGsonRequest<T> extends AbstractGsonRequest<T> {
    private final Object requestObj;

    public PostGsonRequest(String url, Object requestObj, Class<T> responseClass, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(Method.POST, url, responseClass, successListener, listener);
        this.requestObj = requestObj;
    }

    @Override
    public byte[] getBody() {
        return gson.toJson(requestObj).getBytes();
    }
}
