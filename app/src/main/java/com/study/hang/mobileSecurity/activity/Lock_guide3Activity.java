package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class Lock_guide3Activity extends Activity{
    private GestureDetector gestureDetector;
    private EditText et_number;
    private Button bt_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_guide3);
        et_number= (EditText) findViewById(R.id.et_number);
        bt_contact=(Button) findViewById(R.id.choose_number);
        String str=SpUtil.getString(this,"saveNumber");
        if(!TextUtils.isEmpty(str))
            et_number.setText(str);
        bt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent=new Intent(Lock_guide3Activity.this,ContactActivity.class);
                 startActivityForResult(intent,0);
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
                    Intent intent=new Intent(Lock_guide3Activity.this,Lock_guide2Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lock_anim_next2, R.anim.lock_anim_pre2);
                    finish();
                }else if(e1.getRawX()-e2.getRawX()>100) {
                    String str=et_number.getText().toString().trim();
                    if(!TextUtils.isEmpty(str)) {
                        SpUtil.setString(Lock_guide3Activity.this,"saveNumber",str);
                        Intent intent=new Intent(Lock_guide3Activity.this,Lock_guide4Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
                        finish();
                    }else {
                        Toast.makeText(Lock_guide3Activity.this,"请输入安全号码再进行下一步",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


    }
    public void next(View view) {
        String str=et_number.getText().toString().trim();
        if(!TextUtils.isEmpty(str)) {
            SpUtil.setString(this,"saveNumber",str);
            Intent intent=new Intent(this,Lock_guide4Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
            finish();
        }else {
            Toast.makeText(this,"请输入安全号码再进行下一步",Toast.LENGTH_SHORT).show();
        }

    }
    public void pre(View view) {
        Intent intent=new Intent(this,Lock_guide2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lock_anim_next2,R.anim.lock_anim_pre2);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null) {
            return;
        }
        String number=data.getStringExtra("number");
        if(!TextUtils.isEmpty(number)) {
            if(requestCode==0&&resultCode==1) {
                et_number.setText(number);
                SpUtil.setString(Lock_guide3Activity.this,"saveNumber",number);
            }
        }
    }
}
