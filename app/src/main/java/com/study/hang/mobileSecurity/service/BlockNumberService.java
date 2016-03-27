package com.study.hang.mobileSecurity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.android.internal.telephony.ITelephony;
import com.study.hang.db.BlackNumberDao;

import java.lang.reflect.Method;

/**
 * Created by hang on 16/3/27.
 */
public class BlockNumberService extends Service {
    private TelephonyManager manager;
    private PhoneStateListener listener;
    private BlackNumberDao dao;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("拦截服务开启");
        dao=new BlackNumberDao(this);
        manager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener=new mListener();
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener=null;
    }
    private class mListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode=dao.queryMode(incomingNumber);
                    if(!TextUtils.isEmpty(mode)) {
                        if("1".equals(mode)||"3".equals(mode)) {

                            endCall();
                        }
                    }

                    break;

            }
        }

        private void endCall() {
            try {
                System.out.println("================》拦截电话开始");
                Class mClass=BlockNumberService.class.getClassLoader().loadClass("android.os.ServiceManager");
                Method method=mClass.getDeclaredMethod("getService", String.class);
                IBinder iBinder= (IBinder) method.invoke(null,TELECOM_SERVICE);
                ITelephony.Stub.asInterface(iBinder).endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
