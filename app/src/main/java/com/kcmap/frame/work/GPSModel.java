package com.kcmap.frame.work;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPSModel {
    Context ctx;
    LocationManager locationManager;
    long time;
    public AsyncLocationResponse delegate;
    public GPSModel(Context context,long time){
        this.ctx=context;
        this.time=time;
        locationManager=(LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    public void updateToNewLocation(Location location)throws IOException {
        delegate.locateFinish(location);

    }
    @SuppressLint("MissingPermission")
    public void OpenGPS() throws IOException{
        if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            // 查找到服务信息
            this.delegate=(AsyncLocationResponse)ctx;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE); //设置粗略精确度
            criteria.setAltitudeRequired(false);//设置是否需要返回海拔信息
            criteria.setBearingRequired(false);//设置是否需要返回方位信息
            criteria.setCostAllowed(false);//设置是否允许付费服务
            criteria.setPowerRequirement(Criteria.POWER_LOW); //设置电量消耗等级
            criteria.setSpeedRequired(false);//设置是否需要返回速度信息

            String provider = locationManager.getBestProvider(criteria, true); //根据设置的Criteria对象，获取最符合此标准的provider对象

            Location location = locationManager.getLastKnownLocation(provider); //根据当前provider对象获取最后一次位置信息
            if(location!=null){
                updateToNewLocation(location);
            }
            // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
            locationManager.requestLocationUpdates(provider, time*1000, 0,locationListener);
            //增加GPS状态监听器
            //locationManager.addGpsStatusListener(gpsListener);
            Toast.makeText(ctx, "GPS正在定位...为确保精度请不要位于室内。", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ctx, "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity)ctx).startActivityForResult(intent,2); //此为设置完成后返回到获取界面
        }

    }

    public void CloseGPS(){
        if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.removeGpsStatusListener(gpsListener);
            locationManager.removeUpdates(locationListener);
            locationManager=null;
            delegate=null;
            Toast.makeText(ctx, "GPS定位已关闭", Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener locationListener=new LocationListener() {
        //位置发生改变时调用
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if(location!=null){
                try {
                    updateToNewLocation(location);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        //状态改变时调用
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
        //provider启用时调用
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }
        //provider失效时调用
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }
    };

    GpsStatus.Listener gpsListener =new GpsStatus.Listener() {

        public void onGpsStatusChanged(int event) {
            // TODO Auto-generated method stub
            //获取当前状态
            @SuppressLint("MissingPermission")
            GpsStatus gpsstatus=locationManager.getGpsStatus(null);
            switch(event){
                //第一次定位时的事件
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    break;
                //开始定位的事件
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                //发送GPS卫星状态事件
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    //Toast.makeText(ctx, "卫星状态", Toast.LENGTH_SHORT).show();
                    Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();
                    Iterator<GpsSatellite> it=allSatellites.iterator();
                    int count = 0;
                    while(it.hasNext())
                    {
                        count++;
                    }
                    Toast.makeText(ctx, "当前卫星颗数:" + count, Toast.LENGTH_SHORT).show();
                    break;
                //停止定位事件
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.d("Location", "GPS_EVENT_STOPPED");
                    break;
            }
        }
    };
}
