package com.pacific.detect.sensortest.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by fly on 2017/7/22.
 */

public class GsonRequest<T> extends Request<T> {

    private static final String TAG = GsonRequest.class.getSimpleName();

    private final Gson gson = new Gson();

    private final Class<T> cls;

    private final Map<String,String> headers;

    private final Response.Listener<T> listener;

    private int cacheHit ;
    private int cacheExpiry;

    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int method , String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.cls = clazz;
        this.headers = headers;
        this.listener = listener;

        cacheHit = 3*60*60*1000;
        cacheExpiry = 24*60*60*1000;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null?headers:super.getHeaders();
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Response re = Response.success(gson.fromJson(json,cls), RHttpHeaderParser.parseIgnoreCacheHeaders(response,cacheHit,cacheExpiry));
            return re;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }



}
