package com.pacific.detect.sensortest.ui;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pacific.detect.sensortest.location.EnviroDetection;
import com.pacific.detect.sensortest.location.LocationDetection;
import com.pacific.detect.sensortest.location.GPSLocationListener;
import com.pacific.detect.sensortest.location.GPSProviderStatus;

public class AndroidGPSLocationActivity extends AppCompatActivity {

    private final String TAG = "AndroidGPSActivity";
    private TextView mLogInfoTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gpslocation_data);

        LinearLayout VLayout = new LinearLayout(this);
        VLayout.setOrientation(LinearLayout.VERTICAL);

        mLogInfoTextView = new TextView(this);
        mLogInfoTextView.setTextColor(Color.BLACK);
        mLogInfoTextView.setText("info");
        mLogInfoTextView.setTextSize(18);
        mLogInfoTextView.setGravity(1);
        VLayout.addView(mLogInfoTextView);

        setContentView(VLayout);

        // 获取太阳角度
        InitLocLight();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    private MyListener listener = new MyListener();

    class MyListener implements GPSLocationListener {

        @Override
        public void update(double latitude, double longitude, int lum, boolean isInstant)
        {
        }


        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {

                lat = location.getLatitude();
                lng = location.getLongitude();
                if (enviro == null)
                {
                    enviro = new EnviroDetection(lat, lng);

                } else {
                    enviro.updateLocation(lat, lng);
                }

                int gazi = (int) Math.round(enviro.getAzimuth());           //Azimuth   方位角
                int zazi = (int) Math.round(enviro.getZenithAngle());       //Zenith    天顶角

                mLogInfoTextView.setText("instant:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\nName: " + LocationDetection.loci);
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps" == provider) {
                Toast.makeText(getApplicationContext(), "定位类型：" + provider, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
                    Toast.makeText(getApplicationContext(), "GPS开启", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_DISABLED:
                    Toast.makeText(getApplicationContext(), "GPS关闭", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
                    Toast.makeText(getApplicationContext(), "GPS不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(getApplicationContext(), "GPS暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
                    Toast.makeText(getApplicationContext(), "GPS可用啦", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    //--------------------
    //location and enviro
    //---------------------
    private double lat = 0;     // latitude 纬度
    private double lng = 0;     // longitude 经度
    public EnviroDetection enviro = null;
    private LocationDetection appLocationManager = null;
    private double fixedAzimuth = 0;
    private double fixedZenith = 90;

    //--------------------
    //light Detection from location
    //------------------
    private void InitLocLight()
    {
        //setup location provider
        appLocationManager = new LocationDetection(this.getApplicationContext(),this, listener);
        lat = appLocationManager.getLatitude();
        lng = appLocationManager.getLongitude();

        Log.i(TAG," "+lat+":"+lng);

        //default to shenzhen
        if(lat == 0 && lng == 0)
        {
            lat = 33.5946571;
            lng = 130.359499;

            //japan
            //latitude = 33.5946571;
            //longitude = 130.359499;

            enviro = new EnviroDetection(lat, lng);

            if (enviro != null) {
                int gazi = (int) Math.round(enviro.getAzimuth());           //Azimuth   方位角
                int zazi = (int) Math.round(enviro.getZenithAngle());       //Zenith    天顶角

                mLogInfoTextView.setText("default:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\nName: " + LocationDetection.loci);
            }

        } else {
            enviro = new EnviroDetection(lat, lng);

            if (enviro != null) {
                int gazi = (int) Math.round(enviro.getAzimuth());           //Azimuth   方位角
                int zazi = (int) Math.round(enviro.getZenithAngle());       //Zenith    天顶角

                mLogInfoTextView.setText("instant:\n经度：" + lat + "\n纬度：" + lng + "\nAzimuth: " + gazi + "\nZenith: " + zazi + "\nName: " + LocationDetection.loci);
            }
        }
    }
}
