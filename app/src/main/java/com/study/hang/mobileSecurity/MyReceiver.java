package com.study.hang.mobileSecurity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/22.
 */
public class MyReceiver extends BroadcastReceiver {
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"开机成功",Toast.LENGTH_LONG).show();
        boolean setting_lock=SpUtil.getBoolean(context,"setting_lock",false);
        if(setting_lock) {
            String simNum = SpUtil.getString(context, "SimSerialNum");
            String saveNum=SpUtil.getString(context,"saveNum");
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String newNum = tm.getSimSerialNumber() + "aaaaaa";
            SmsManager smsManager=SmsManager.getDefault();
            if (!newNum.equals(simNum)) {
                if (!TextUtils.isEmpty(saveNum)) {
                    smsManager.sendTextMessage(saveNum, null, "SIM卡变更啦！", null, null);
                }
            }
        }
    }
}
