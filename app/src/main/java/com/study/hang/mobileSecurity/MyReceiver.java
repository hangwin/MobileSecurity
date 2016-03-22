package com.study.hang.mobileSecurity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/22.
 */
public class MyReceiver extends BroadcastReceiver {
    private TelephonyManager tm;
    @Override
    public void onReceive(Context context, Intent intent) {
           System.out.println("开机啦-------------》");
           String simNum= SpUtil.getString(context,"SimSerialNum");
           tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
           String newNum=tm.getSimSerialNumber();
           if (!newNum.equals(simNum)) {
               Toast.makeText(context,"SIM卡变更啦",Toast.LENGTH_SHORT);
           }
    }
}
