package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class Lock_guide1Activity extends Activity{
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_guide1);

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
                if(e2.getRawX()-e1.getRawX()>0) {

                }else if(e1.getRawX()-e2.getRawX()>100) {
                    Intent intent=new Intent(Lock_guide1Activity.this,Lock_guide2Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
                    finish();

                }
                return false;
            }
        });

    }
    public void next(View view) {
        Intent intent=new Intent(this,Lock_guide2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
