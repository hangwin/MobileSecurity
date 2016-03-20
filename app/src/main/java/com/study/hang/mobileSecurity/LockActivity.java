package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class LockActivity extends Activity{
    private TextView tv_enterGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasset= SpUtil.getBoolean(this,"hasSetLock",false);
        if (hasset) {  //如果已经进入过设置向导，则直接进入防盗页面
            setContentView(R.layout.lock_page);
            tv_enterGuide= (TextView) findViewById(R.id.tv_enterGuide);
            tv_enterGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(LockActivity.this,Lock_guide1Activity.class);
                    startActivity(intent);
                }
            });

        }else {         //如果没有进入过向导页面，则进入向导页面
            SpUtil.setBoolean(this,"hasSetLock",true);
            Intent intent=new Intent(this,Lock_guide1Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
        }
    }
}
