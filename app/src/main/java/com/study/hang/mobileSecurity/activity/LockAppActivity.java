package com.study.hang.mobileSecurity.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.SpUtil;

/**
 * Created by hang on 16/4/9.
 */
public class LockAppActivity extends Activity {
    private EditText psd;
    private Button ok;
    private Button cancle;
    private ImageView image;
    private TextView tv;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_app);
        psd= (EditText) findViewById(R.id.et_psd);
        ok= (Button) findViewById(R.id.ok);
        cancle= (Button) findViewById(R.id.cancle);
        tv= (TextView) findViewById(R.id.appname);
        image= (ImageView) findViewById(R.id.icon);
        final String curPackageName=getIntent().getStringExtra("curPackageName");
        PackageManager pm=getPackageManager();
        if(!TextUtils.isEmpty(curPackageName)) {
            try {
               ApplicationInfo info= pm.getApplicationInfo(curPackageName, 0);
               image.setImageDrawable(info.loadIcon(pm));
               tv.setText(info.loadLabel(pm));
            } catch (PackageManager.NameNotFoundException e) {
                image.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                tv.setText(curPackageName);
                e.printStackTrace();
            }
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=psd.getText().toString();
                if(!TextUtils.isEmpty(password)) {
                    if(password.equals(SpUtil.getString(LockAppActivity.this,"AppPsd"))) {
                        Intent intent = new Intent();
                        intent.setAction("com.hang.stopWatchdog");
                        intent.putExtra("resultApp", curPackageName);
                        sendBroadcast(intent);
                        finish();
                    }else {
                        Toast.makeText(LockAppActivity.this,"密码错误",Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(LockAppActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.MONKEY");
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
        super.onBackPressed();
    }
}
