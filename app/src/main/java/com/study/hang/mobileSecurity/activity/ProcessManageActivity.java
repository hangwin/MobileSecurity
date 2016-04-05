package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.ProcessEntity;
import com.study.hang.util.ProcessinfoUtil;
import com.study.hang.util.SystemInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/5.
 */
public class ProcessManageActivity extends Activity {
    private TextView process_count;
    private TextView memory;
    private ListView listView;
    private LinearLayout progress;
    private List<ProcessEntity> list,userprogress,systemprogress;
    private mAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processmanage);
        process_count= (TextView) findViewById(R.id.process_count);
        memory= (TextView) findViewById(R.id.memory);
        listView= (ListView) findViewById(R.id.lv_processinfo);
        progress= (LinearLayout) findViewById(R.id.progress);
        process_count.setText("活动进程:"+ SystemInfoUtil.getRunningProcessnum(this)+"个");
        memory.setText("剩余/总内存:"+SystemInfoUtil.getAvailableMemory(this)+"/"+SystemInfoUtil.getTotalMemory(this));
        getData();
    }

    public void getData() {
        progress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                list= ProcessinfoUtil.getProcessInfo(ProcessManageActivity.this);
                userprogress=new ArrayList<ProcessEntity>();
                systemprogress=new ArrayList<ProcessEntity>();
                for(ProcessEntity process:list) {
                    if (process.isuserprocess()) {
                        userprogress.add(process);
                    }else {
                        systemprogress.add(process);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.INVISIBLE);
                        if(adapter==null) {
                            adapter=new mAdapter();
                            listView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();

    }


    class mAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userprogress.size()+systemprogress.size()+2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProcessEntity process=null;
            if(position==0) {
                TextView tv;
                if(convertView!=null&&(convertView instanceof TextView)) {
                    tv= (TextView) convertView;
                }else {
                    tv=new TextView(ProcessManageActivity.this);
                }
                tv.setText("用户程序("+userprogress.size()+")");
                tv.setPadding(16, 5, 0, 5);
                tv.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                return tv;
            }else if(position==userprogress.size()+1) {
                TextView tv;
                if(convertView!=null&&(convertView instanceof TextView)) {
                    tv= (TextView) convertView;
                }else {
                    tv=new TextView(ProcessManageActivity.this);
                }
                tv.setText("系统程序("+systemprogress.size()+")");
                tv.setPadding(16,5,0,5);
                tv.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                return tv;
            }else {
                if(position<=userprogress.size()) {
                    process=userprogress.get(position-1);
                }else if(position>=userprogress.size()+2) {
                    {
                        process = systemprogress.get(position - userprogress.size() - 2);
                        //System.out.println("--===>"+app.toString());
                    }
                }

            }
            View view;
            ViewHolder holder;
            if(convertView!=null&&convertView instanceof RelativeLayout) {
                view=convertView;
                holder= (ViewHolder) view.getTag();

            }else {
                holder=new ViewHolder();
                view= LayoutInflater.from(ProcessManageActivity.this).inflate(R.layout.process_item,null);
                holder.image= (ImageView) view.findViewById(R.id.app_icon);
                holder.processName= (TextView) view.findViewById(R.id.processname);
                holder.processMem= (TextView) view.findViewById(R.id.processmem);
                view.setTag(holder);

            }
            holder.image.setImageDrawable(process.getIcon());
            holder.processName.setText(process.getName());
            holder.processMem.setText("内存大小："+ Formatter.formatFileSize(ProcessManageActivity.this,process.getMemsize()));
            return view;
        }
    }
    class ViewHolder {
        ImageView image;
        TextView processName;
        TextView processMem;

    }
}
