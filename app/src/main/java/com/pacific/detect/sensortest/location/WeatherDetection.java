package com.pacific.detect.sensortest.location;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.pacific.detect.sensortest.bean.WeatherBean;
import com.pacific.detect.sensortest.net.GsonRequest;

/**
 * Created by fly on 2017/7/22.
 */

public class WeatherDetection {


    private static final String TAG = "WeatherDetection";
    private static final String k = "3ba3e48f2b29489f997d911ab81112e1";
    private static final String HOST = "https://free-api.heweather.com/v5/now";

    private int luminosity = 180;
    RequestQueue queue;

    public void getWeather(String city, Context context){
        if (queue == null){
            queue = Volley.newRequestQueue(context);
        }
        GsonRequest request = new GsonRequest(Request.Method.GET, HOST+"?city="+city+"&key="+k,
                WeatherBean.class, null, new Response.Listener<WeatherBean>() {
            @Override
            public void onResponse(WeatherBean weatherBean) {
                calLuminosity(weatherBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.getCause());
                luminosity = 180;

            }
        });
        queue.add(request);
    }

    /**
     *
     * @param weatherBean weatherbean
     */
    private void calLuminosity(WeatherBean weatherBean){
        if (weatherBean == null || weatherBean.getHeWeather5() == null || weatherBean.getHeWeather5().size() <= 0){
            luminosity = 180;
            return;
        }
        WeatherBean.HeWeather5Bean.NowBean nowBean = weatherBean.getHeWeather5().get(0).getNow();
        int wCode = 0;
        try {
            wCode = Integer.parseInt(nowBean.getCond().getCode());
        }catch (Exception e){
            e.printStackTrace();
        }

        if (wCode == 100){//sunny
            luminosity = 255;
        }else if (wCode == 101){//cloudy
            luminosity = 230;
        }else if (wCode == 102){//few clouds
            luminosity = 250;
        }else if (wCode == 103){//party cloudy
            luminosity = 240;
        }else if (wCode == 104){//overcast
            luminosity = 180;
        }
        else if (200 <=wCode && wCode < 204 ){//light breeze
            luminosity = 220;
        }else if (205 <= wCode && wCode < 214){//storm
            luminosity = 160;
        }
        else if (300 <= wCode && wCode < 314){//rain
            luminosity = 180;
        }
        else if (400 <= wCode && wCode < 408){//snow
            luminosity = 190;
        }
        else if (500 <= wCode && wCode < 408){//foggy
            luminosity = 160;
        }
        else {
            luminosity = 183;
        }

    }

    public int getLuminosity() {
        return luminosity;
    }
}
