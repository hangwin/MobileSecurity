package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.util.SpUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private static final int URL_ERROR = 0;
    private static final int IO_ERROR = 1;
    private static final int JSON_ERROR = 2;
    private static final int ENTER_HOME = 3;
    private static final int SHOW_UPDATE = 4;
    private TextView tv_version;
    private TextView tv_update;
    private String lastestVersion;
    private String apkUrl;
    private String description;
    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
              switch (msg.what) {
                  case URL_ERROR:
                      Toast.makeText(SplashActivity.this,"服务器地址出错",Toast.LENGTH_SHORT).show();
                      enterHome();
                      break;
                  case IO_ERROR:
                      Toast.makeText(SplashActivity.this,"网络连接出错",Toast.LENGTH_SHORT).show();
                      enterHome();
                      break;
                  case JSON_ERROR:
                      Toast.makeText(SplashActivity.this,"JSON解析出错",Toast.LENGTH_SHORT).show();
                      enterHome();
                      break;
                  case ENTER_HOME:
                      enterHome();
                      break;
                  case SHOW_UPDATE:
                      showUpdateDialog();
                      break;
              }
        }
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("更新提示");
        builder.setMessage(description);
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        })    ;
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                update();

                //enterHome();

            }
        });
        builder.show();
    }

    private void update() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FinalHttp fhttp = new FinalHttp();
            Toast.makeText(this, Environment.getExternalStorageDirectory().getAbsolutePath(),Toast.LENGTH_SHORT).show();
            fhttp.download(apkUrl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobilesafe.apk", new AjaxCallBack<File>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    System.out.println(strMsg);
                    Toast.makeText(SplashActivity.this,"下载失败，请重试",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long count, long current) {
                    super.onLoading(count, current);
                    tv_update.setVisibility(TextView.VISIBLE);
                    tv_update.setText(count/current+"%");
                }

                @Override
                public void onSuccess(File file) {
                    super.onSuccess(file);
                    install(file);
                }
            }) ;

        }  else {
            Toast.makeText(SplashActivity.this,"找不到sdcard,请检查是否有sdcard",Toast.LENGTH_SHORT).show();

        }
    }

    private void install(File file) {
        Intent intent=new Intent();
        intent.setAction("android.intent.aciton.VIEW");
       // intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果是第一次启动，则进入进到页面
      if(!SpUtil.getBoolean(this,"isFirstOpen")) {
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            setContentView(R.layout.splash_layout);
            tv_version= (TextView) findViewById(R.id.tv_version);
            tv_update= (TextView) findViewById(R.id.tv_progress);
            if(!TextUtils.isEmpty(getVersion()))
                tv_version.setText("版本："+getVersion());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getResultFromServer();


                }
            }).start();




         }
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

    // 从服务器得到更新信息
    public String getResultFromServer() {
        long start=System.currentTimeMillis();
        Message mes=Message.obtain();
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
                result=result.substring(0,result.indexOf("}")+1);
                System.out.println("result-->:" + result);
                JSONObject jsonObject=new JSONObject(result);
                lastestVersion=jsonObject.getString("version");
                apkUrl=jsonObject.getString("apkUrl");
                description=jsonObject.getString("description");
                if (lastestVersion.equals(getVersion())) {
                    mes.what=ENTER_HOME;
                }else {
                    mes.what=SHOW_UPDATE;
                }

                is.close();
                baos.close();
                return result;
            }
        } catch (MalformedURLException e) {
            mes.what=URL_ERROR;
            e.printStackTrace();
        } catch (IOException e) {
            mes.what=IO_ERROR;
            e.printStackTrace();
        } catch (JSONException e) {
            mes.what=JSON_ERROR;
            e.printStackTrace();
        }finally {
            long end=System.currentTimeMillis();
            try {
                Thread.sleep(2000-(end-start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendMessage(mes);
        }

        return null;
    }
    public void enterHome(){
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
