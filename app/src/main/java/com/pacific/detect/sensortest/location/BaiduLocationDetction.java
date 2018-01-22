package com.pacific.detect.sensortest.location;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by fly on 2017/7/20.
 */

public class BaiduLocationDetction {

    public static  String TAG = "BaiduLocationDetction";
    private double latitude = 33.5946571;
    private double longitude = 130.359499;
    private String city;

    public boolean isInstantLocation() {
        return isInstantLocation;
    }

    private boolean isInstantLocation = false;

    LocationClient locationClient;
    private WeatherDetection weatherDetection;
    private Context context;

    GPSLocationListener mGpsListener;

    /**
     *
     * init locationclient
     * @param context
     */
    public BaiduLocationDetction(Context context, GPSLocationListener listener) {
        this.context = context;
        this.mGpsListener = listener;

        locationClient = new LocationClient(context);
        locationClient.setLocOption(initLocation());
        locationClient.registerLocationListener(new BaiduLocationListener());
        startLocation();
    }


    public void startLocation(){
        if (locationClient != null){
            locationClient.start();
        }
    }

    public void stopLocation(){
        if (locationClient != null){
            locationClient.stop();
        }
    }

    /**
     * config LocationClient,set scanspan 1000,default 0
     *
     * @return location config
     */
    private LocationClientOption initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setScanSpan(60*1000);
        return option;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public void setWeatherDetection(WeatherDetection weatherDetection) {
        this.weatherDetection = weatherDetection;
    }

    class BaiduLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation
                    ){
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                city = bdLocation.getAddress().city;

                isInstantLocation = true;

            }else {
                Log.i(TAG, "onReceiveLocation failed");
                latitude = 33.5946571;
                longitude = 130.359499;
                city = "深圳";

                isInstantLocation = false;
            }

            Log.i(TAG, "onReceiveLocation: la="+latitude+"lon="+longitude+"city="+city);

            if (weatherDetection != null && !TextUtils.isEmpty(city)){
                weatherDetection.getWeather(city,context);
            }

            if (null != mGpsListener)
            {
                mGpsListener.update(latitude, longitude, weatherDetection.getLuminosity(), isInstantLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
