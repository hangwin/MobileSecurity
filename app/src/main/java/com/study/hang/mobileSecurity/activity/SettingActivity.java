package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.study.hang.mobileSecurity.R;
import com.study.hang.mobileSecurity.service.FindAddressService;
import com.study.hang.ui.ClickItem;
import com.study.hang.ui.SettingItem;
import com.study.hang.util.ServiceUtil;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/3/19.
 * 设置中心
 */
public class SettingActivity  extends Activity{

    private SettingItem settingItem;
    private SettingItem findaddressItem;
    private ClickItem style;
    @Override
    protected void onResume() {
        super.onResume();
        findaddressItem.setMian("是否显示电话归属地");
        if(ServiceUtil.isServiceAlive(this,getPackageName())) {
            findaddressItem.setDesc("已开启电话归属地显示");
            findaddressItem.setIsChecked(true);
        }else {
            findaddressItem.setDesc("已关闭电话归属地显示");
            findaddressItem.setIsChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_center);
        settingItem= (SettingItem) findViewById(R.id.update_item);
        boolean isupdate=SpUtil.getBoolean(this,"isUpdate",true);
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
                if(isChecked) {

                    settingItem.setDesc("已开启自动升级");
                }else {

                    settingItem.setDesc("已关闭自动升级");
                }

            }
        });

        findaddressItem= (SettingItem) findViewById(R.id.showaddress_item);
        findaddressItem.setMian("是否显示电话归属地");
        if(ServiceUtil.isServiceAlive(this,getPackageName())) {
            findaddressItem.setDesc("已开启电话归属地显示");
            findaddressItem.setIsChecked(true);
        }else {
            findaddressItem.setDesc("已关闭电话归属地显示");
            findaddressItem.setIsChecked(false);
        }
        findaddressItem.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    findaddressItem.setDesc("已开启电话归属地显示");
                    Intent intent=new Intent(SettingActivity.this, FindAddressService.class);
                    startService(intent);
                }else {
                    findaddressItem.setDesc("已关闭电话归属地显示");
                    Intent intent=new Intent(SettingActivity.this, FindAddressService.class);
                    stopService(intent);
                }
            }
        });

        style= (ClickItem) findViewById(R.id.style);
        final String[] str={"浅蓝色","浅绿色","深绿色","浅紫色"};
        final int position=SpUtil.getInt(SettingActivity.this,"style_id");
        style.setDesc(str[position]);
        style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(SettingActivity.this);
                builder.setSingleChoiceItems(str, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtil.setInt(SettingActivity.this,"style_id",which);
                        style.setDesc(str[which]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取 消",null);
                builder.show();
            }

        });


    }
}
