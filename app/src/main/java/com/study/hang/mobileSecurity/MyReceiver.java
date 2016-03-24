package com.study.hang.mobileSecurity;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
    private DevicePolicyManager devicePolicyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"开机成功",Toast.LENGTH_LONG).show();
        boolean setting_lock=SpUtil.getBoolean(context, "setting_lock", false);
        if(setting_lock) {
          /*  String simNum = SpUtil.getString(context, "SimSerialNum");
            String saveNum=SpUtil.getString(context,"saveNum");
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String newNum = tm.getSimSerialNumber() + "aaaaaa";
            SmsManager smsManager=SmsManager.getDefault();
            if (!newNum.equals(simNum)) {
                if (!TextUtils.isEmpty(saveNum)) {
                    smsManager.sendTextMessage(saveNum, null, "SIM卡变更啦！", null, null);
                }
            } */
           // getAdmin(context);
            //devicePolicyManager= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            //devicePolicyManager.wipeData(2);  擦除数据，回复出厂设置；
            Intent serviceIntent=new Intent(context,ScreenService.class);
            context.startService(serviceIntent);
        } else {
           // removeAdmin(context);
        }
    }

    private void getAdmin(Context context) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName mDeviceAdminSample=new ComponentName(context,MyAdmin.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "开启设备管理员权限");
        context.startActivity(intent);
    }

   private void removeAdmin(Context context) {
       devicePolicyManager.removeActiveAdmin(new ComponentName(context,MyAdmin.class));

   }
}
