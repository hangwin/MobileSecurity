package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.study.hang.com.study.hang.ui.SettingItem;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class Lock_guide2Activity extends Activity{
    private GestureDetector gestureDetector;
    private SettingItem settingItem;
    private TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_guide2);
        settingItem= (SettingItem) findViewById(R.id.bind_simcard);
        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        settingItem.setMian("是否绑定SIM卡");
        String simNum=SpUtil.getString(this,"SimSerialNum");
        if (TextUtils.isEmpty(simNum)) {
            settingItem.setDesc("没有绑定SIM卡");
            settingItem.setIsChecked(false);
        }else {
            settingItem.setDesc("已绑定SIM卡");
            settingItem.setIsChecked(true);
        }


        settingItem.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.setBoolean(Lock_guide2Activity.this,"isBindSIM",isChecked);
                if(isChecked) {
                    String num=telephonyManager.getSimSerialNumber();//得到SIM卡的序列号
                    SpUtil.setString(Lock_guide2Activity.this,"SimSerialNum",num);
                    settingItem.setDesc("已绑定SIM卡");
                }else {
                    SpUtil.setString(Lock_guide2Activity.this,"SimSerialNum",null);
                    settingItem.setDesc("没有绑定SIM卡");
                }
            }
        });
        gestureDetector=new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getRawX()-e1.getRawX()>100) {
                    Intent intent=new Intent(Lock_guide2Activity.this,Lock_guide1Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lock_anim_next2, R.anim.lock_anim_pre2);
                    finish();
                }else if(e1.getRawX()-e2.getRawX()>100) {
                    Intent intent=new Intent(Lock_guide2Activity.this,Lock_guide3Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
                    finish();
                }
                return false;
            }
        });


    }
    public void next(View view) {
        Intent intent=new Intent(this,Lock_guide3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
        finish();
    }
    public void pre(View view) {
        Intent intent=new Intent(this,Lock_guide1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lock_anim_next2,R.anim.lock_anim_pre2);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
