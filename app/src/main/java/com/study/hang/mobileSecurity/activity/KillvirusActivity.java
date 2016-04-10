package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.hang.db.VirusDao;
import com.study.hang.mobileSecurity.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by hang on 16/4/10.
 */
public class KillvirusActivity extends Activity {
    private static final int SCAN = 1;
    private static final int FINISH = 2;
    private ProgressBar pb;
    private TextView tv;
    private LinearLayout content;
    public static int count=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TextView text=new TextView(KillvirusActivity.this);
            scaninfo info= (scaninfo) msg.obj;
            switch (msg.what){
                case SCAN:

                    tv.setText("正在扫描："+info.name);
                    if(info.isVirus) {
                        count++;
                        text.setTextColor(Color.RED);
                        text.setText("发现病毒："+info.name);
                    }else {
                        text.setTextColor(Color.BLACK);
                        text.setText("扫描安全："+info.name);
                    }
                    content.addView(text,0);
                break;
                case FINISH:
                    tv.setText("扫描完成,共发现"+count+"个可疑文件");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kill_virus);
        pb= (ProgressBar) findViewById(R.id.pb);
        tv= (TextView) findViewById(R.id.tv);
        content= (LinearLayout) findViewById(R.id.content);
        scan();
    }

    public void scan() {
        final PackageManager manager=getPackageManager();
        tv.setText("正在初始化病毒扫描引擎");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<ApplicationInfo> infos=manager.getInstalledApplications(0);
                pb.setMax(infos.size());
                int progress=0;
                for(ApplicationInfo info:infos) {
                    scaninfo s=new scaninfo();
                    s.packagename=info.packageName;
                    s.name=info.loadLabel(manager).toString();
                    String srcDir=info.sourceDir;
                    String md5=getMd5(srcDir);
                    if(!TextUtils.isEmpty(md5)) {
                        if(VirusDao.find(KillvirusActivity.this,md5)){
                            s.isVirus=true;
                        }else {
                            s.isVirus=false;
                        }
                    }
                    pb.setProgress(progress++);
                    Message mes=Message.obtain();
                    mes.what=SCAN;
                    mes.obj=s;
                    handler.sendMessage(mes);
                }
                Message m=Message.obtain();
                m.what=FINISH;
                handler.sendMessage(m);

            }
        }).start();

    }

    public String getMd5(String path) {

        try {
            MessageDigest digest=MessageDigest.getInstance("md5");
            File file=new File(path);
            FileInputStream fis=new FileInputStream(file);
            byte[] buffer=new byte[1024];
            int len=-1;
            StringBuilder res=new StringBuilder();
            while ((len=fis.read(buffer))!=-1) {
               digest.update(buffer,0,len);
            }
            byte[] result=digest.digest();
            for(byte b:result) {
                int temp=b&0xff;
                String str=Integer.toHexString(temp);
                if(str.length()==1) {
                    res.append("0");
                }
                res.append(str);
            }
            return res.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    class scaninfo{
        String packagename;
        String name;
        boolean isVirus;
    }
}
