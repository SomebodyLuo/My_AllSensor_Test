package com.pacific.detect.sensortest.ui;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

// gyroscope & accelerator & compass
public class GyroAccRotCounterActivity extends AppCompatActivity implements SensorEventListener{

    private final String TAG = "GyroAccRotCounterActivity";

    private int spaceHeight = 20;
    private TextView mGyroscopeSensorTextView = null;
    private TextView mAccelerometerSensorTextView = null;
    private TextView mRotationVecSensorTextView = null;
    private TextView mStepCounterSensorTextView = null;

    private TextView mLogInfoTextView = null;

    private SensorManager mSensorManager;

    private Sensor mGyroscopeSensor;
    // 将纳秒转化为秒
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];

    private Sensor mAccelerometerSensor;
    private Sensor mRotationVecSensor;

    private Sensor mCompassSensor;

    private float[] rotVecValues = null;
    private float[] rotvecR = new float[9], rotQ = new float[4];
    private float[] rotvecOrientValues = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // UI设置
//        setContentView(R.layout.activity_gyroscope);
        LinearLayout HLayout = new LinearLayout(this);
        HLayout.setOrientation(LinearLayout.VERTICAL);
        HLayout.setGravity(2);

        // --------------------- mGyroscopeSensorTextView ----------------------
        mGyroscopeSensorTextView = new TextView(this);
        mGyroscopeSensorTextView.setTextColor(Color.RED);
        mGyroscopeSensorTextView.setText("gyroscope");
        mGyroscopeSensorTextView.setTextSize(18);
        mGyroscopeSensorTextView.setGravity(2);
        HLayout.addView(mGyroscopeSensorTextView);

        Space space1 = new Space(this);
        space1.setMinimumWidth(1);
        space1.setMinimumHeight(spaceHeight);

        HLayout.addView(space1);

        // --------------------- mAccelerometerSensorTextView ----------------------
        mAccelerometerSensorTextView = new TextView(this);
        mAccelerometerSensorTextView.setTextColor(Color.RED);
        mAccelerometerSensorTextView.setText("accelerometer");
        mAccelerometerSensorTextView.setTextSize(18);
        mAccelerometerSensorTextView.setGravity(2);
        HLayout.addView(mAccelerometerSensorTextView);

        Space space2 = new Space(this);
        space2.setMinimumWidth(1);
        space2.setMinimumHeight(spaceHeight);

        HLayout.addView(space2);

        // --------------------- mRotationVecSensorTextView ----------------------
        mRotationVecSensorTextView = new TextView(this);
        mRotationVecSensorTextView.setTextColor(Color.RED);
        mRotationVecSensorTextView.setText("Rotation Vec");
        mRotationVecSensorTextView.setTextSize(18);
        mRotationVecSensorTextView.setGravity(2);
        HLayout.addView(mRotationVecSensorTextView);

        Space space3 = new Space(this);
        space3.setMinimumWidth(1);
        space3.setMinimumHeight(spaceHeight);

        HLayout.addView(space3);


        // --------------------- mStepCounterSensorTextView ----------------------
        mStepCounterSensorTextView = new TextView(this);
        mStepCounterSensorTextView.setTextColor(Color.RED);
        mStepCounterSensorTextView.setText("StepCounter");
        mStepCounterSensorTextView.setTextSize(18);
        mStepCounterSensorTextView.setGravity(2);
        HLayout.addView(mStepCounterSensorTextView);

        Space space4 = new Space(this);
        space4.setMinimumWidth(1);
        space4.setMinimumHeight(spaceHeight);

        HLayout.addView(space4);


        LinearLayout VLayout = new LinearLayout(this);
        VLayout.setOrientation(LinearLayout.VERTICAL);

        VLayout.addView(HLayout);

        mLogInfoTextView = new TextView(this);
        mLogInfoTextView.setTextColor(Color.BLACK);
        mLogInfoTextView.setText("info");
        mLogInfoTextView.setTextSize(12);
        mLogInfoTextView.setGravity(1);
        VLayout.addView(mLogInfoTextView);


        setContentView(VLayout);

        // sensor
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // this is Compass / Magnetic
        mRotationVecSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mCompassSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onResume()
    {
        mLogInfoTextView.setText("");
        // 注册
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotationVecSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mCompassSensor, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //注销
        mSensorManager.unregisterListener(this, mGyroscopeSensor);
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        mSensorManager.unregisterListener(this, mRotationVecSensor);
        mSensorManager.unregisterListener(this, mCompassSensor);

        super.onPause();
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_GYROSCOPE:
            {
                if(timestamp != 0) {

//                    String data = "x = " + event.values[0] + ";\n y = " + event.values[1] + ";\n z = " + event.values[2];
//                    mGyroscopeSensorTextView.setText(data);

                    // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                    final float dT = (event.timestamp - timestamp) * NS2S;

                    // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                    angle[0] += event.values[0] * dT;
                    angle[1] += event.values[1] * dT;
                    angle[2] += event.values[2] * dT;

                    // 将弧度转化为角度
                    float anglex = (float) Math.toDegrees(angle[0]);
                    float angley = (float) Math.toDegrees(angle[1]);
                    float anglez = (float) Math.toDegrees(angle[2]);

                    String data = "----Gyroscope(with integration by dT):\n yaw = " + anglez + ";\n pitch = " + anglex + ";\n roll = " + angley;
                    mGyroscopeSensorTextView.setText(data);
                }

                //将当前时间赋值给timestamp
                timestamp = event.timestamp;

                break;
            }

            case Sensor.TYPE_ACCELEROMETER:
            {
                String data = "----Accelerator:\n x = " + event.values[0] + ";\n y = " + event.values[1] + ";\n z = " + event.values[2];
                mAccelerometerSensorTextView.setText(data);
                break;
            }

            case Sensor.TYPE_ROTATION_VECTOR:
            {
                if(rotVecValues == null){
                    rotVecValues = new float[event.values.length];
                }

                for(int i = 0; i < rotVecValues.length; i++){
                    rotVecValues[i] = event.values[i];
                }

                if(rotVecValues != null){
                    SensorManager.getQuaternionFromVector(rotQ, rotVecValues);
                    SensorManager.getRotationMatrixFromVector(rotvecR, rotVecValues);
                    SensorManager.getOrientation(rotvecR, rotvecOrientValues);
                }

                String msg = String.format("----Rotation Vector Sensor:\n yaw %7.3f\n pitch %7.3f\n roll %7.3f\n w,x,y,z %6.2f,%6.2f,%6.2f,%6.2f\n",
                    Math.toDegrees(rotvecOrientValues[0]),
                    Math.toDegrees(rotvecOrientValues[1]),
                    Math.toDegrees(rotvecOrientValues[2]),
                    rotQ[0],rotQ[1],rotQ[2],rotQ[3]);

                mRotationVecSensorTextView.setText(msg);

                break;
            }

            case Sensor.TYPE_STEP_COUNTER:
            {

//                Log.i(TAG, "value lenth = " + event.values.length);
//                String data = "----Step Counter:\n x = " + event.values[0] + ";\n y = " + event.values[1] + ";\n z = " + event.values[2];
                String data = "----Step Counter:\n";
                for (int i = 0; i < event.values.length; i++)
                {
                    data += i + " = " + event.values[i] + "\n";
                }
                Log.i(TAG, "data: " + data);
                mStepCounterSensorTextView.setText(data);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        String info = sensor.getName() + " accuracy changed: " + accuracy + "\n";
        mLogInfoTextView.append(info);
    }

}
