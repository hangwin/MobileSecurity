package com.study.hang.mobileSecurity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.db.FindNumberDB;
import com.study.hang.mobileSecurity.R;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/26.
 */
public class FindAddressService extends Service {
    private TelephonyManager telephonyManager;
    private TextView textView;
    private myListener listener;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private View view;
    private float startX,startY;
    private WindowManager.LayoutParams params;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("服务开启");
        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener=new myListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        receiver=new OutCallReceiver();
        registerReceiver(receiver,filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener=null;
        unregisterReceiver(receiver);
        receiver=null;

    }

    private class myListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:  //监听响铃状态
                    String result= FindNumberDB.findNumber(FindAddressService.this,incomingNumber);
                    //Toast.makeText(FindAddressService.this,result,Toast.LENGTH_LONG).show();
                    myToast(result);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (windowManager!=null)
                    windowManager.removeView(view);
            }
        }
    }

    private class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number=getResultData();
            String result=FindNumberDB.findNumber(FindAddressService.this,number);
            //Toast.makeText(FindAddressService.this,result,Toast.LENGTH_LONG).show();
            myToast(result);
        }
    }
    long[] times=new long[2];
    public void myToast(String str) {

        int[] imageId=new int[] {R.drawable.light_blue,R.drawable.light_green,R.drawable.light_gray,R.drawable.deep_green,R.drawable.light_white};
        int position= SpUtil.getInt(FindAddressService.this,"style_id");
        view=View.inflate(this, R.layout.call,null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println("========click=========");

                System.arraycopy(times,1,times,0,times.length-1);
                times[times.length-1]=SystemClock.uptimeMillis();
                if(times[0]>= (SystemClock.uptimeMillis()-500)) {
                    params.x=windowManager.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
                    windowManager.updateViewLayout(view,params);
                    SpUtil.setFloat(FindAddressService.this,"lastX",params.x);
                }


            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float newX,newY,dx,dy;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX= event.getRawX();
                        startY= event.getRawY();
                        event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        newX=event.getRawX();
                        newY=event.getRawY();
                        dx=newX-startX;
                        dy=newY-startY;
                        params.x+=dx;
                        params.y-=dy;
                        if(params.x<0) {
                            params.x=0;
                        }
                        if(params.y<0) {
                            params.y=0;
                        }
                        if(params.x>windowManager.getDefaultDisplay().getWidth()-view.getWidth()) {
                            params.x=windowManager.getDefaultDisplay().getWidth()-view.getWidth();
                        }
                        if(params.y>windowManager.getDefaultDisplay().getHeight()-view.getHeight()) {
                            params.y=windowManager.getDefaultDisplay().getHeight()-view.getHeight();
                        }
                        windowManager.updateViewLayout(view,params);
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtil.setFloat(FindAddressService.this,"lastX",params.x);
                        SpUtil.setFloat(FindAddressService.this,"lastY",params.y);

                        break;
                }
                return false;
            }
        });
        view.setBackgroundResource(imageId[position]);
        textView= (TextView) view.findViewById(R.id.show_area);
        textView.setText(str);
        textView.setTextSize(22);
        textView.setTextColor(Color.BLACK);

        windowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.x= (int) SpUtil.getFloat(FindAddressService.this, "lastX");
        params.y= (int) SpUtil.getFloat(FindAddressService.this,"lastY");
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity= Gravity.LEFT+Gravity.BOTTOM;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        windowManager.addView(view,params);

    }
}
