package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.mobileSecurity.service.BlockNumberService;
import com.study.hang.mobileSecurity.service.FindAddressService;
import com.study.hang.mobileSecurity.service.watchdogService;
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
    private SettingItem blocknum;
    private SettingItem watchdog;
    private ClickItem style;
    private Dialog dialog;
    @Override
    protected void onResume() {
        super.onResume();
        findaddressItem.setMian("是否显示电话归属地");
        if(ServiceUtil.isServiceAlive(this,"com.study.hang.mobileSecurity.service.FindAddressService")) {
            findaddressItem.setDesc("已开启电话归属地显示");
            findaddressItem.setIsChecked(true);
        }else {
            findaddressItem.setDesc("已关闭电话归属地显示");
            findaddressItem.setIsChecked(false);
        }
        if(ServiceUtil.isServiceAlive(this,"com.study.hang.mobileSecurity.service.watchdogService")) {
            watchdog.setDesc("已开启看门狗");
            watchdog.setIsChecked(true);
        }else {
            watchdog.setDesc("已关闭看门狗");
            watchdog.setIsChecked(false);
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

        watchdog= (SettingItem) findViewById(R.id.watchdog);
        watchdog.setMian("是否开启看门狗");
        if(ServiceUtil.isServiceAlive(this,"com.study.hang.mobileSecurity.service.watchdogService")) {
            watchdog.setDesc("已开启看门狗");
            watchdog.setIsChecked(true);
        }else {
            watchdog.setDesc("已关闭看门狗");
            watchdog.setIsChecked(false);
        }
        watchdog.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 final Intent intent=new Intent(SettingActivity.this,watchdogService.class);
                 if(isChecked) {
                     String temp=SpUtil.getString(SettingActivity.this,"AppPsd");
                     System.out.println("========>"+temp);
                     if(TextUtils.isEmpty(temp)) {
                         AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                         View view= LayoutInflater.from(SettingActivity.this).inflate(R.layout.setpassword,null);
                         TextView til= (TextView) view.findViewById(R.id.til);
                         til.setText("请先设置程序锁的密码");
                         final EditText ed_psd= (EditText) view.findViewById(R.id.ed_psd);
                         final EditText ed_confimpsd= (EditText) view.findViewById(R.id.ed_confirmpsd);
                         Button bt_ok= (Button) view.findViewById(R.id.bt_ok);
                         Button bt_cancle= (Button) view.findViewById(R.id.bt_cancel);
                         bt_ok.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 String psd1=ed_psd.getText().toString();
                                 String psd2=ed_confimpsd.getText().toString();
                                 if(!TextUtils.isEmpty(psd1)&&!TextUtils.isEmpty(psd2)) {
                                     if(psd1.equals(psd2)) {
                                         SpUtil.setString(SettingActivity.this,"AppPsd",psd1);
                                         startService(intent);
                                         dialog.dismiss();
                                     }else {
                                         Toast.makeText(SettingActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                                     }
                                 }else {
                                     Toast.makeText(SettingActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();

                                 }
                             }
                         });
                         bt_cancle.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 if (dialog != null) {
                                     watchdog.setIsChecked(false);
                                     dialog.dismiss();
                                 }
                             }
                         });
                         builder.setView(view);
                         dialog=builder.show();

                     }else {
                         startService(intent);
                     }
                 }else {
                     stopService(intent);
                 }
            }
        });
        style= (ClickItem) findViewById(R.id.style);
        final String[] str={"天空蓝","浅绿色","浅灰色","深绿色","亮白色"};
        final int position=SpUtil.getInt(SettingActivity.this,"style_id");
        style.setDesc(str[position]);
        style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setSingleChoiceItems(str, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtil.setInt(SettingActivity.this, "style_id", which);
                        style.setDesc(str[which]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取 消", null);
                builder.show();
            }

        });

        blocknum= (SettingItem) findViewById(R.id.blocknumber);
        blocknum.setMian("是否开启黑名单拦截");
        if(ServiceUtil.isServiceAlive(this,"com.study.hang.mobileSecurity.service.BlockNumberService")) {
            blocknum.setDesc("已开启黑名单拦截");
            blocknum.setIsChecked(true);
        }else {
            blocknum.setDesc("已关闭黑名单拦截");
            blocknum.setIsChecked(false);
        }
        blocknum.tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                blocknum.setIsChecked(isChecked);
                Intent intent = null;
                if (isChecked) {
                    blocknum.setDesc("已开启黑名单拦截");
                    intent = new Intent(SettingActivity.this, BlockNumberService.class);
                    startService(intent);
                } else {
                    blocknum.setDesc("已关闭黑名单拦截");
                    intent = new Intent(SettingActivity.this, BlockNumberService.class);
                    stopService(intent);
                }

            }
        });

    }
}
