package com.pacific.detect.sensortest.location;

import android.util.Log;

import java.util.GregorianCalendar;

import com.pacific.detect.sensortest.calc.AzimuthZenithAngle;
import com.pacific.detect.sensortest.calc.Grena3;


/**
 * Created by ibii on 7/19/17.
 */

public class EnviroDetection
{
    private final String TAG = "luoyouren";
    public AzimuthZenithAngle sunPosition = null;

    public EnviroDetection(double lat, double lon)
    {
        GregorianCalendar cal = new GregorianCalendar();
        sunPosition =  Grena3.calculateSolarPosition(cal,lat, lon,70,1000,20);
        Log.e(TAG, "Found Sun position  as " + sunPosition.getAzimuth() + ", " + sunPosition.getZenithAngle());
    }

    public double getAzimuth()
    {
        return sunPosition.getAzimuth();
    }

    public double getZenithAngle()
    {

        double zenith = sunPosition.getZenithAngle();
        //change angles into degrees
        zenith = 90.0f -zenith;

        return zenith;
    }
}
