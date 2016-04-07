package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.study.hang.mobileSecurity.R;
import com.study.hang.mobileSecurity.service.LockService;
import com.study.hang.util.ServiceUtil;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 2016/4/6.
 */
public class ProgressSettingActivity extends Activity {
    private CheckBox isShow,lockClean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_setting);
        isShow= (CheckBox) findViewById(R.id.isShow);
        lockClean= (CheckBox) findViewById(R.id.lockClean);
        isShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.setBoolean(ProgressSettingActivity.this,"isShowSystemProcess",isChecked);
            }
        });
        lockClean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent=new Intent(ProgressSettingActivity.this, LockService.class);
                if(isChecked) {
                    System.out.println("================>"+isChecked);
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isShow.setChecked(SpUtil.getBoolean(ProgressSettingActivity.this,"isShowSystemProcess",false));
        if(ServiceUtil.isServiceAlive(this,"com.study.hang.mobileSecurity.service.LockService")) {
            System.out.println("============>ServiceAlive");
            lockClean.setChecked(true);
        }else {
            lockClean.setChecked(false);
            System.out.println("==========>ServicenotAlive");
        }
    }
}
