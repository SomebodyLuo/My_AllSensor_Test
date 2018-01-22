package com.pacific.detect.sensortest.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pacific.detect.sensortest.R;

//import eu.kudan.kudan.ARActivity;
//import eu.kudan.kudan.ARImageTrackable;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 请点这个
        findViewById(R.id.btn_gyro_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GyroAccRotCounterActivity.class);
            }
        });

        // 下面这些页面全是测试用的
        findViewById(R.id.btn_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AndroidGPSLocationActivity.class);
            }
        });

        findViewById(R.id.btn_baidu_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BaiduLocationActivity.class);
            }
        });

        findViewById(R.id.btn_camera_renderer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(CameraActivity.class);
            }
        });

        findViewById(R.id.btn_cam_adjust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(CamAdjustActivity.class);
            }
        });

        findViewById(R.id.btn_faceu_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(CameraTrackActivity.class);
            }
        });

        findViewById(R.id.btn_cam_3d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(Camera3DActivity.class);
            }
        });

        findViewById(R.id.btn_cam_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(CameraRecordActivity.class);
            }
        });

        findViewById(R.id.btn_record_3d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(Record3DActivity.class);
            }
        });

        findViewById(R.id.btn_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(NavigationActivity.class);
            }
        });

        //-----------------------------
        //If no permissions set,saved
        //-----------------------------
        if (null == savedInstanceState)
        {
            permissionsRequest();
        }
    }


    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    /**
     * Request code for camera permissions.
     */
    private static final int REQUEST_CAMERA_PERMISSIONS = 1;

    /**
     * Permissions required to take a picture.
     */
    private static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,

    };

    //-------------------------------
    //Check Permissions & if set
    //-------------------------------
    public void permissionsRequest()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
        }
        else
        {
            Log.i(TAG, "permissions is ok!");
        }
    }

    //--------------------------------------
    //On Permission acceptance if not set
    //---------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA_PERMISSIONS:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {

                    Log.i(TAG, "permissions is ok!");
                }
                else
                {

                    throw new RuntimeException("Camera permissions must be granted to function.");
                }
            }
        }
    }
}
