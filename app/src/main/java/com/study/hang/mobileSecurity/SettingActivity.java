package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.study.hang.ui.SettingItem;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/19.
 * 设置中心
 */
public class SettingActivity  extends Activity{

    private SettingItem settingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_center);
        settingItem= (SettingItem) findViewById(R.id.update_item);
        boolean isupdate=SpUtil.getBoolean(this,"isUpdate",true);

        System.out.println("outside--------->："+isupdate);
        settingItem.setIsChecked(isupdate);
        if(isupdate) {
            settingItem.setDesc("已开启自动升级");
        }else {
            settingItem.setDesc("已关闭自动升级");
        }
        settingItem.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.setBoolean(SettingActivity.this, "isUpdate", isChecked);
                System.out.println("--------------->isUpdate" + isChecked);
                if(isChecked) {

                    settingItem.setDesc("已开启自动升级");
                }else {

                    settingItem.setDesc("已关闭自动升级");
                }

            }
        });

    }
}
