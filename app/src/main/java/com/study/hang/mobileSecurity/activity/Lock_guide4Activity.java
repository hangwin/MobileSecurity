package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.study.hang.mobileSecurity.R;
import com.study.hang.ui.SettingItem;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class Lock_guide4Activity extends Activity{
    private GestureDetector gestureDetector;
    private SettingItem setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_guide4);
        setting= (SettingItem) findViewById(R.id.setting_lock);
        boolean setting_lock=SpUtil.getBoolean(this,"setting_lock",false);
        setting.setMian("是否开启手机防盗");
        if(setting_lock) {
            setting.setIsChecked(true);
            setting.setDesc("已开启手机防盗");
        }else {
            setting.setIsChecked(false);
            setting.setDesc("已关闭手机防盗");
        }
        setting.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.setBoolean(Lock_guide4Activity.this, "setting_lock", isChecked);
                if (isChecked) {
                    setting.setIsChecked(true);
                    setting.setDesc("已开启手机防盗");
                } else {
                    setting.setIsChecked(false);
                    setting.setDesc("已关闭手机防盗");
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
                    Intent intent=new Intent(Lock_guide4Activity.this,Lock_guide3Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lock_anim_next2, R.anim.lock_anim_pre2);
                    finish();
                }else if(e1.getRawX()-e2.getRawX()>100) {
                    boolean hasset=SpUtil.getBoolean(Lock_guide4Activity.this,"hasSetLock",false);
                    if(!hasset) {
                        SpUtil.setBoolean(Lock_guide4Activity.this,"hasSetLock",true);
                        Intent intent=new Intent(Lock_guide4Activity.this,LockActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
                    }
                    finish();
                }
                return false;
            }
        });


    }
    public void pre(View view) {
        Intent intent=new Intent(this,Lock_guide3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lock_anim_next2, R.anim.lock_anim_pre2);
        finish();
    }
    public void next(View view) {
        boolean hasset=SpUtil.getBoolean(this,"hasSetLock",false);
        if(!hasset) {
            SpUtil.setBoolean(this,"hasSetLock",true);
            Intent intent=new Intent(this,LockActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
        }
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
