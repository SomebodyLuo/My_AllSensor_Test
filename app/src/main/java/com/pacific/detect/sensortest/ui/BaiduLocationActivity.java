package com.pacific.detect.sensortest.ui;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pacific.detect.sensortest.location.BaiduLocationDetction;
import com.pacific.detect.sensortest.location.EnviroDetection;
import com.pacific.detect.sensortest.location.GPSLocationListener;
import com.pacific.detect.sensortest.location.WeatherDetection;

public class BaiduLocationActivity extends AppCompatActivity {

    private final String TAG = "BaiduLocationActivity";

    private BaiduLocationDetction baiduLocationDetction;
    private WeatherDetection weatherDetection;

    //--------------------
    //environ
    //--------------------
    private double lat = 0;
    private double lng = 0;
    private int luminosity = 0;
    private EnviroDetection enviro = null;


    private TextView mLogInfoTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_baidu_location);

        LinearLayout VLayout = new LinearLayout(this);
        VLayout.setOrientation(LinearLayout.VERTICAL);

        mLogInfoTextView = new TextView(this);
        mLogInfoTextView.setTextColor(Color.BLACK);
        mLogInfoTextView.setText("info");
        mLogInfoTextView.setTextSize(18);
        mLogInfoTextView.setGravity(1);
        VLayout.addView(mLogInfoTextView);

        setContentView(VLayout);

        InitLocLight();
    }


    //--------------------
    //light Detection from location
    //------------------
    private void InitLocLight()
    {
        weatherDetection = new WeatherDetection();
        baiduLocationDetction = new BaiduLocationDetction(getApplicationContext(), listener);
        baiduLocationDetction.setWeatherDetection(weatherDetection);

        lat = baiduLocationDetction.getLatitude();
        lng = baiduLocationDetction.getLongitude();

        luminosity = weatherDetection.getLuminosity();
        Log.i(TAG, "InitLocLight: luminoty="+luminosity+" lat="+lat +" lng="+lng);

        enviro = new EnviroDetection(lat,lng);

        if (enviro != null) {
            int gazi = (int) Math.round(enviro.getAzimuth());           //Azimuth   方位角
            int zazi = (int) Math.round(enviro.getZenithAngle());       //Zenith    天顶角

            if (baiduLocationDetction.isInstantLocation())
            {
                mLogInfoTextView.setText("instant:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\n光强: " + luminosity);
            } else {

                mLogInfoTextView.setText("default:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\n光强: " + luminosity);
            }
        }

    }

    private MyListener listener = new MyListener();

    class MyListener implements GPSLocationListener {

        @Override
        public void update(double latitude, double longitude, int lum, boolean isInstant)
        {
            lat = latitude;
            lng = longitude;
            luminosity = lum;

            if (enviro == null)
            {
                enviro = new EnviroDetection(lat, lng);

            } else {
                enviro.updateLocation(lat, lng);
            }

            int gazi = (int) Math.round(enviro.getAzimuth());           //Azimuth   方位角
            int zazi = (int) Math.round(enviro.getZenithAngle());       //Zenith    天顶角

            if (isInstant)
            {
                mLogInfoTextView.setText("instant:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\n光强: " + luminosity);
            } else {

                mLogInfoTextView.setText("default:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\n光强: " + luminosity);
            }

        }

        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {

            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
//            if ("gps" == provider) {
//                Toast.makeText(getApplicationContext(), "定位类型：" + provider, Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
//            switch (gpsStatus) {
//                case GPSProviderStatus.GPS_ENABLED:
//                    Toast.makeText(getApplicationContext(), "GPS开启", Toast.LENGTH_SHORT).show();
//                    break;
//                case GPSProviderStatus.GPS_DISABLED:
//                    Toast.makeText(getApplicationContext(), "GPS关闭", Toast.LENGTH_SHORT).show();
//                    break;
//                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
//                    Toast.makeText(getApplicationContext(), "GPS不可用", Toast.LENGTH_SHORT).show();
//                    break;
//                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
//                    Toast.makeText(getApplicationContext(), "GPS暂时不可用", Toast.LENGTH_SHORT).show();
//                    break;
//                case GPSProviderStatus.GPS_AVAILABLE:
//                    Toast.makeText(getApplicationContext(), "GPS可用啦", Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }
}
