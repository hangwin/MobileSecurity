package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.study.hang.db.FindNumberDB;
import com.study.hang.mobileSecurity.R;

/**
 * Created by hang on 16/3/26.
 */
public class FindNumberActivity extends Activity {
    private EditText et_number;
    private Button bt_find;
    private TextView tv_show;
    private Vibrator vibrator;    //振动器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findnumber);
        et_number= (EditText) findViewById(R.id.et_number);
        bt_find= (Button) findViewById(R.id.bt_find);
        tv_show= (TextView) findViewById(R.id.tv_show);
        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=et_number.getText().toString().trim();
                if(TextUtils.isEmpty(number)) {
                    Animation shake = AnimationUtils.loadAnimation(FindNumberActivity.this, R.anim.shake);
                    findViewById(R.id.et_number).startAnimation(shake);
                    vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                    return;
                }
                String result=FindNumberDB.findNumber(FindNumberActivity.this,number);
                tv_show.setText(result);
            }
        });
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    String result = FindNumberDB.findNumber(FindNumberActivity.this, s.toString());
                    tv_show.setText(result);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
