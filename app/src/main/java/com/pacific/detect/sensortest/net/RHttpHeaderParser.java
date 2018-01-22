package com.pacific.detect.sensortest.net;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by fly on 2017/7/22.
 */

public class RHttpHeaderParser extends HttpHeaderParser {


    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response, int cacheHit, int cacheExpiry) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;

        long serverDate = 0;

        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long ttl = now + cacheExpiry;

        final long softExpire;
        if(cacheHit > 0){
            softExpire = now + cacheHit;
        }else{
            softExpire = ttl;
        }

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

}
