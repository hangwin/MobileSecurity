package com.study.hang.mobileSecurity.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.AppEntity;
import com.study.hang.util.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/3.
 */
public class SoftwareManageActivity extends Activity implements View.OnClickListener{
    private TextView tv_memory;
    private TextView tv_sd;
    private ListView lv;
    private List<AppEntity> list,userapp,systemapp;
    private ProgressBar pb;
    private TextView status;
    private PopupWindow popup;
    private LinearLayout unstall,run,share;
    private AppEntity app;
    private  myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sofewaremanage);
        tv_memory= (TextView) findViewById(R.id.memory);
        tv_sd= (TextView) findViewById(R.id.sdcard);
        lv= (ListView) findViewById(R.id.lv_appinfo);
        pb= (ProgressBar) findViewById(R.id.pb);
        status= (TextView) findViewById(R.id.status);
        tv_memory.setText("内存可用空间："+getAvailableSpace(Environment.getDataDirectory().getAbsolutePath()));
        tv_sd.setText("SD卡可用空间："+getAvailableSpace(Environment.getExternalStorageDirectory().getAbsolutePath()));
        getdata();
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userapp != null && systemapp != null) {
                    if (firstVisibleItem > userapp.size()) {
                        status.setText("系统程序(" + systemapp.size() + ")");
                    } else {
                        status.setText("用户程序(" + userapp.size() + ")");
                    }
                }
                if(popup!=null&&popup.isShowing()) {
                    popup.dismiss();
                    popup=null;
                }

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(popup!=null&&popup.isShowing()) {
                    popup.dismiss();
                    popup=null;
                }

                if(position==0||position==userapp.size()+1) {
                    return;
                }else if(position<=userapp.size()) {
                    app=userapp.get(position-1);
                    pupuwindow(parent, view);
                }else {
                    app=systemapp.get(position-2-userapp.size());
                    pupuwindow(parent, view);
                }
            }
        });
    }

    private void pupuwindow(AdapterView<?> parent, View view) {
        View contentView= LayoutInflater.from(SoftwareManageActivity.this).inflate(R.layout.popu_dialog,null);
        unstall= (LinearLayout) contentView.findViewById(R.id.uninstall);
        run= (LinearLayout) contentView.findViewById(R.id.run);
        share= (LinearLayout)contentView.findViewById(R.id.share);
        unstall.setOnClickListener(SoftwareManageActivity.this);
        run.setOnClickListener(SoftwareManageActivity.this);
        share.setOnClickListener(SoftwareManageActivity.this);
        popup=new PopupWindow(contentView,300,120);
        int[] location=new int[2];
        view.getLocationInWindow(location);
        popup.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,location[0]+50,location[1]-60);
    }

    private void getdata() {
        pb.setVisibility(ProgressBar.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                list= AppInfoUtil.getAppInfoList(SoftwareManageActivity.this);
                userapp=new ArrayList<AppEntity>();
                systemapp=new ArrayList<AppEntity>();
                for(AppEntity app:list) {
                    if(app.isUser()) {
                        System.out.println("=======>"+app.getAppName());
                        userapp.add(app);
                    }else {
                        systemapp.add(app);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        if(adapter==null) {
                            adapter = new myAdapter();
                            lv.setAdapter(adapter);
                        }
                        else {
                            System.out.println("==========>数据变化了");
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        }.start();
    }


   /*
    *获取可用空间
    */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public String getAvailableSpace(String path) {

        StatFs statfs=new StatFs(path);
        long size= statfs.getAvailableBytes();
        String available=Formatter.formatFileSize(this,size);
        return available;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popup!=null&&popup.isShowing()) {
            popup.dismiss();
            popup=null;
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.uninstall:
                if(app.isUser()) {
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setAction("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + app.getPackageName()));
                    startActivityForResult(intent, 0);
                }else {
                    Toast.makeText(this,"系统程序需要root权限才能卸载",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.run:
                PackageManager pm=getPackageManager();
                intent= pm.getLaunchIntentForPackage(app.getPackageName());
                if(intent!=null)
                    startActivity(intent);
                else
                    Toast.makeText(SoftwareManageActivity.this, "启动失败", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                intent=new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "跟你分享一个软件" + app.getAppName() + ",很有趣，快去下载吧!");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getdata();

    }

    private class myAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppEntity app=null;
            if(position==0) {
                TextView tv;
                if(convertView!=null&&(convertView instanceof TextView)) {
                    tv= (TextView) convertView;
                }else {
                    tv=new TextView(SoftwareManageActivity.this);
                }
                tv.setText("用户程序("+userapp.size()+")");
                tv.setPadding(16, 5, 0, 5);
                tv.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                return tv;
            }else if(position==userapp.size()+1) {
                TextView tv;
                if(convertView!=null&&(convertView instanceof TextView)) {
                    tv= (TextView) convertView;
                }else {
                    tv=new TextView(SoftwareManageActivity.this);
                }
                tv.setText("系统程序("+systemapp.size()+")");
                tv.setPadding(16,5,0,5);
                tv.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                return tv;
            }else {
                if(position<=userapp.size()) {
                    app=userapp.get(position-1);
                }else if(position>=userapp.size()+2) {
                    {
                        app = systemapp.get(position - userapp.size() - 2);
                        //System.out.println("--===>"+app.toString());
                    }
                }

            }
            View view;
            ViewHolder holder=new ViewHolder();
            if(convertView!=null&&(convertView instanceof RelativeLayout)) {
                view=convertView;
                holder= (ViewHolder) view.getTag();
            }else {
                view= LayoutInflater.from(SoftwareManageActivity.this).inflate(R.layout.appinfo_item,null);
                holder.icon= (ImageView) view.findViewById(R.id.app_icon);
                holder.tv_appname= (TextView) view.findViewById(R.id.appname);
                holder.tv_location= (TextView) view.findViewById(R.id.isInRom);
                view.setTag(holder);
            }
            holder.icon.setImageDrawable(app.getIcon());
            holder.tv_appname.setText(app.getAppName());
            holder.tv_location.setText(app.isInRom()?"手机内存":"外部存储");
            return view;
        }
        class ViewHolder {
            ImageView icon;
            TextView tv_appname;
            TextView tv_location;
        }
    }
}
