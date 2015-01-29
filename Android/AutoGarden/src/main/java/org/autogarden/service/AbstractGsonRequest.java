package org.autogarden.service;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGsonRequest<T> extends Request<T> {
    protected final Gson gson = new Gson();
    private Class<T> responseClass;
    private Response.Listener<T> successListener;

    public AbstractGsonRequest(int method, String url, Class<T> responseClass, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.responseClass = responseClass;
        this.successListener = successListener;
    }

    public AbstractGsonRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String token = TokenManager.getInstance().getToken();
        if(token != null) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("token", token);
            return headers;
        }
        return super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            if (responseClass != null) {
                String json = new String(
                        response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(
                        gson.fromJson(json, responseClass), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (successListener != null) {
            successListener.onResponse(response);
        }
    }
}
