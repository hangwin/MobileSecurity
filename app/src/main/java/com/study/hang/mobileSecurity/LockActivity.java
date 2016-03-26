package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.study.hang.ui.SettingItem;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/20.
 * 手机防盗功能
 */
public class LockActivity extends Activity{
    private TextView tv_enterGuide;
    private SettingItem setting;
    private TextView tv_safenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasset= SpUtil.getBoolean(this,"hasSetLock",false);
        if (hasset) {  //如果已经进入过设置向导，则直接进入防盗页面
            setContentView(R.layout.lock_page);
            tv_safenum= (TextView) findViewById(R.id.tv_savenum);
            String safenum=SpUtil.getString(this,"saveNumber");
            if(TextUtils.isEmpty(safenum)) {
                tv_safenum.setText("尚未设置");
            }else {
                tv_safenum.setText(safenum);
            }
            tv_enterGuide= (TextView) findViewById(R.id.tv_enterGuide);
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
                    SpUtil.setBoolean(LockActivity.this,"setting_lock",isChecked);
                    if(isChecked) {
                        setting.setIsChecked(true);
                        setting.setDesc("已开启手机防盗");
                    }else {
                        setting.setIsChecked(false);
                        setting.setDesc("已关闭手机防盗");
                    }
                }
            });
            tv_enterGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(LockActivity.this,Lock_guide1Activity.class);
                    startActivity(intent);
                }
            });

        }else {         //如果没有进入过向导页面，则进入向导页面

            Intent intent=new Intent(this,Lock_guide1Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.lock_anim_pre, R.anim.lock_anim_next);
        }
    }
}
