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
    private int updateTimes = 0;

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

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(6*1000);

        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);




//        LocationClientOption option = new LocationClientOption();
//
//        option.setLocationMode(LocationMode.Hight_Accuracy);
////可选，设置定位模式，默认高精度
////LocationMode.Hight_Accuracy：高精度；
////LocationMode. Battery_Saving：低功耗；
////LocationMode. Device_Sensors：仅使用设备；
//
//        option.setCoorType("bd09ll");
////可选，设置返回经纬度坐标类型，默认gcj02
////gcj02：国测局坐标；
////bd09ll：百度经纬度坐标；
////bd09：百度墨卡托坐标；
////海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
//
//        option.setScanSpan(1000);
////可选，设置发起定位请求的间隔，int类型，单位ms
////如果设置为0，则代表单次定位，即仅定位一次，默认为0
////如果设置非0，需设置1000ms以上才有效
//
//        option.setOpenGps(true);
////可选，设置是否使用gps，默认false
////使用高精度和仅用设备两种定位模式的，参数必须设置为true
//
//        option.setLocationNotify(true);
////可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
//
//        option.setIgnoreKillProcess(false);
////可选，定位SDK内部是一个service，并放到了独立进程。
////设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//
//        option.SetIgnoreCacheException(false);
////可选，设置是否收集Crash信息，默认收集，即参数为false
//
//        option.setWifiCacheTimeOut(5*60*1000);
////可选，7.2版本新增能力
////如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
//
//        option.setEnableSimulateGps(false);
////可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
//
//        mLocationClient.setLocOption(option);
////mLocationClient为第二步初始化过的LocationClient对象
////需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
////更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

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

            float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = bdLocation.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = bdLocation.getLocType();


            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            //61	GPS定位结果，GPS定位成功     TypeGpsLocation
            //62	无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
            //63	网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
            //66	离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果       TypeOffLineLocation
            //68	网络连接失败时，查找本地离线定位时对应的返回结果
            //161	网络定位结果，网络定位成功       TypeNetWorkLocation
            //162	请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
            //167	服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位
            //505	AK不存在或者非法，请按照说明文档重新申请AK
            Log.i(TAG, "radius = " + radius + "; coorType = " + coorType + "; errorCode = " + errorCode);
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation
                    ){
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                city = bdLocation.getAddress().city;

                isInstantLocation = true;
                updateTimes++;

            } else {
                Log.i(TAG, "onReceiveLocation failed");
                latitude = 33.5946571;
                longitude = 130.359499;
                city = "深圳";

                isInstantLocation = false;
            }

            Log.i(TAG, "onReceiveLocation: la= " + latitude + "; lon= " + longitude + "; city= " + city);

            if (weatherDetection != null && !TextUtils.isEmpty(city)){
                weatherDetection.getWeather(city,context);
            }

            if (null != mGpsListener)
            {
//                mGpsListener.update(latitude, longitude, weatherDetection.getLuminosity(), isInstantLocation, updateTimes, errorCode);
                mGpsListener.update(bdLocation, weatherDetection.getLuminosity(), isInstantLocation, updateTimes);
            }
        }
    }
}
