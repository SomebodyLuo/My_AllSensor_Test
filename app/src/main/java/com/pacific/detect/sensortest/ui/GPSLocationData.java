package com.pacific.detect.sensortest.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.pacific.detect.sensortest.location.EnviroDetection;
import com.pacific.detect.sensortest.location.LocationDetection;

public class GPSLocationData extends AppCompatActivity {

    private final String TAG = "GPSLocationData";
    private TextView mLogInfoTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gpslocation_data);


//        LinearLayout HLayout = new LinearLayout(this);
//        HLayout.setOrientation(LinearLayout.VERTICAL);
//        HLayout.setGravity(2);
//
//        // --------------------- mGyroscopeSensorTextView ----------------------
//        mGyroscopeSensorTextView = new TextView(this);
//        mGyroscopeSensorTextView.setTextColor(Color.RED);
//        mGyroscopeSensorTextView.setText("gyroscope");
//        mGyroscopeSensorTextView.setTextSize(18);
//        mGyroscopeSensorTextView.setGravity(2);
//        HLayout.addView(mGyroscopeSensorTextView);
//
//        Space space1 = new Space(this);
//        space1.setMinimumWidth(1);
//        space1.setMinimumHeight(spaceHeight);
//
//        HLayout.addView(space1);
//
//        // --------------------- mAccelerometerSensorTextView ----------------------
//        mAccelerometerSensorTextView = new TextView(this);
//        mAccelerometerSensorTextView.setTextColor(Color.RED);
//        mAccelerometerSensorTextView.setText("accelerometer");
//        mAccelerometerSensorTextView.setTextSize(18);
//        mAccelerometerSensorTextView.setGravity(2);
//        HLayout.addView(mAccelerometerSensorTextView);
//
//        Space space2 = new Space(this);
//        space2.setMinimumWidth(1);
//        space2.setMinimumHeight(spaceHeight);
//
//        HLayout.addView(space2);
//
//        // --------------------- mRotationVecSensorTextView ----------------------
//        mRotationVecSensorTextView = new TextView(this);
//        mRotationVecSensorTextView.setTextColor(Color.RED);
//        mRotationVecSensorTextView.setText("Rotation Vec");
//        mRotationVecSensorTextView.setTextSize(18);
//        mRotationVecSensorTextView.setGravity(2);
//        HLayout.addView(mRotationVecSensorTextView);
//
//        Space space3 = new Space(this);
//        space3.setMinimumWidth(1);
//        space3.setMinimumHeight(spaceHeight);
//
//        HLayout.addView(space3);
//
//
//        // --------------------- mStepCounterSensorTextView ----------------------
//        mStepCounterSensorTextView = new TextView(this);
//        mStepCounterSensorTextView.setTextColor(Color.RED);
//        mStepCounterSensorTextView.setText("StepCounter");
//        mStepCounterSensorTextView.setTextSize(18);
//        mStepCounterSensorTextView.setGravity(2);
//        HLayout.addView(mStepCounterSensorTextView);
//
//        Space space4 = new Space(this);
//        space4.setMinimumWidth(1);
//        space4.setMinimumHeight(spaceHeight);
//
//        HLayout.addView(space4);


        LinearLayout VLayout = new LinearLayout(this);
        VLayout.setOrientation(LinearLayout.VERTICAL);

        mLogInfoTextView = new TextView(this);
        mLogInfoTextView.setTextColor(Color.BLACK);
        mLogInfoTextView.setText("info");
        mLogInfoTextView.setTextSize(18);
        mLogInfoTextView.setGravity(1);
        VLayout.addView(mLogInfoTextView);


        setContentView(VLayout);
    }

    @Override
    public void onResume()
    {
        getlocLight();

        super.onResume();
    }


    //--------------------
    //location and enviro
    //---------------------
    private double lat = 0;
    private double lng = 0;
    public EnviroDetection enviro = null;
    private LocationDetection appLocationManager = null;
    private double fixedAzimuth = 0;
    private double fixedZenith = 90;

    //--------------------
    //light Detection from location
    //------------------
    private void getlocLight()
    {
        //setup location provider
        appLocationManager = new LocationDetection(this.getApplicationContext(),this);
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
        }

        Log.i(TAG," "+lat+":"+lng);

        if (false) {
            enviro = new EnviroDetection(lat, lng);

            if (enviro != null) {
                int gazi = (int) Math.round(enviro.getAzimuth());
                int zazi = (int) Math.round(enviro.getZenithAngle());

                mLogInfoTextView.setText("AZI: " + gazi + " | ZEN: " + zazi + " | " + LocationDetection.loci);
            }
        } else {
            mLogInfoTextView.setText("latitude: " + lat + " | longitude: " + lng + " | " + LocationDetection.loci);
        }


    }


}
