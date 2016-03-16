package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.study.hang.util.SpUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 启动页
 * Created by hang on 2016/3/14.
 */
public class SplashActivity extends Activity {
    private TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果是第一次启动，则进入进到页面
     /*   if(!SpUtil.getBoolean(this,"isFirstOpen")) {
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }else { */
            setContentView(R.layout.splash_layout);
            tv_version= (TextView) findViewById(R.id.tv_version);
            if(!TextUtils.isEmpty(getVersion()))
                tv_version.setText("版本："+getVersion());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=getResultFromServer();

                }
            }).start();



          //  SpUtil.setBoolean(this,"isFirstOpen",true);

     //   }
    }

    //得到程序的版本信息
    public String getVersion() {
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo info=packageManager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getResultFromServer() {
        try {
            URL url=new URL(getResources().getString(R.string.version_url));
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(4000);
            int code=con.getResponseCode();
            if(code==200) {
                InputStream is=con.getInputStream();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[] bytes=new byte[1024];
                int len=0;
                while((len=is.read(bytes))!=-1) {
                   baos.write(bytes,0,len);
                }
                String result=baos.toString();
               // result=result.substring(0,result.lastIndexOf("}"));
                Log.i("----------------->",result);
                System.out.println("result-->:"+result);
                is.close();
                baos.close();
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
