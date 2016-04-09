package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.AppEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/9.
 */
public class TrafficStatisticsActivity extends Activity {
    private ListView lv;
    private mAdapter adapter;
    private List<AppEntity> list;
    private PackageManager manager;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic);
        lv= (ListView) findViewById(R.id.lv_trafficinfo);
        pb= (ProgressBar) findViewById(R.id.pb);
        list=new ArrayList<AppEntity>();
        adapter=new mAdapter();
        manager=getPackageManager();
        getData();

    }

    private void getData() {
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ApplicationInfo> infos=manager.getInstalledApplications(0);
                for (ApplicationInfo info:infos) {
                    if(TrafficStats.getUidRxBytes(info.uid)!=0){
                        AppEntity entity = new AppEntity();
                        entity.setIcon(info.loadIcon(manager));
                        entity.setAppName(info.loadLabel(manager).toString());
                        entity.setPackageName(info.packageName);
                        entity.setUid(info.uid);
                        list.add(entity);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }).start();

    }

    private class mAdapter extends BaseAdapter{

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
            View view;
            ViewHolder holder;
            if(convertView!=null) {
                view=convertView;
                holder= (ViewHolder) view.getTag();
            }else {
                view= LayoutInflater.from(TrafficStatisticsActivity.this).inflate(R.layout.traffic_item,null);
                holder=new ViewHolder();
                holder.icon= (ImageView) view.findViewById(R.id.app_icon);
                holder.appName= (TextView) view.findViewById(R.id.appname);
                holder.traffic= (TextView) view.findViewById(R.id.traffic);
                holder.totalTraffic= (TextView) view.findViewById(R.id.total);
                view.setTag(holder);
            }
            holder.icon.setImageDrawable(list.get(position).getIcon());
            holder.appName.setText(list.get(position).getAppName());
            long down=TrafficStats.getUidRxBytes(list.get(position).getUid());
            long send=TrafficStats.getUidTxBytes(list.get(position).getUid());
            holder.traffic.setText("上传："+ Formatter.formatFileSize(TrafficStatisticsActivity.this,send)+"   下载："+Formatter.formatFileSize(TrafficStatisticsActivity.this,down));
            holder.totalTraffic.setText("总流量："+Formatter.formatFileSize(TrafficStatisticsActivity.this,send+down));
            return view;
        }

    }
    class ViewHolder{
        ImageView icon;
        TextView appName,traffic,totalTraffic;
    }
}
