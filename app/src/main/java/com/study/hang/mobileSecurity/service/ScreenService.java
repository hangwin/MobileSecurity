package com.study.hang.mobileSecurity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/24.
 */
public class ScreenService extends Service {
    private LocationManager locationManager;
    private LocationListener listener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("====================服务开启了===============");
        IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        ScreenService.this.registerReceiver(new mReceiver(), mScreenOffFilter);
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        String provider=locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 30000, 100,listener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                    String jing="jingdu:"+location.getLongitude()+"\n";
                    String wei="weidu:"+location.getLatitude();
                    SpUtil.setString(ScreenService.this,"location",jing+wei);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
        listener=null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    private class mReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("==============Lock Screen==============");
            String position=SpUtil.getString(ScreenService.this, "location");
            String saveNumber=SpUtil.getString(ScreenService.this,"saveNum");
            System.out.println(position);
            if (!TextUtils.isEmpty(saveNumber)&&!TextUtils.isEmpty(position)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(saveNumber, null, position, null, null);
            }

        }
    }
}
