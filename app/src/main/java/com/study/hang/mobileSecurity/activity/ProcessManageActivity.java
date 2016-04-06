package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.ProcessEntity;
import com.study.hang.util.ProcessinfoUtil;
import com.study.hang.util.SpUtil;
import com.study.hang.util.SystemInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/5.
 */
public class ProcessManageActivity extends Activity {
    private TextView process_count;
    private TextView memory,status;
    private ListView listView;
    private LinearLayout progress;
    private List<ProcessEntity> list,userprogress,systemprogress;
    private mAdapter adapter;
    private int runningProcessNum;
    private long availableMem,totalMem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processmanage);
        process_count= (TextView) findViewById(R.id.process_count);
        memory= (TextView) findViewById(R.id.memory);
        listView= (ListView) findViewById(R.id.lv_processinfo);
        progress= (LinearLayout) findViewById(R.id.progress);
        status= (TextView) findViewById(R.id.status);
        runningProcessNum=SystemInfoUtil.getRunningProcessnum(this);
        availableMem=SystemInfoUtil.getAvailableMemory(this);
        totalMem=SystemInfoUtil.getTotalMemory(this);
        process_count.setText("活动进程:"+ runningProcessNum+"个");
        memory.setText("剩余/总内存:"+Formatter.formatFileSize(this, availableMem)+"/"+Formatter.formatFileSize(this,totalMem));
        getData();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (systemprogress != null && userprogress != null) {
                    if (firstVisibleItem > userprogress.size()) {
                        status.setText("系统进程:" + systemprogress.size() + "个");
                    } else {
                        status.setText("用户进程:" + userprogress.size() + "个");
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessEntity process;
                ViewHolder holder = (ViewHolder) view.getTag();
                if (position == 0 || position == userprogress.size() + 1) return;
                if (position <= userprogress.size()) {
                    process = userprogress.get(position - 1);
                    if(process.getPackageName().equals(getPackageName())) {
                        return;
                    }
                    if (process.isChecked()) {
                        process.setIsChecked(false);
                    } else {
                        process.setIsChecked(true);
                    }
                    holder.checkBox.setChecked(process.isChecked());
                } else {
                    process = systemprogress.get(position - userprogress.size() - 2);
                    if (process.isChecked()) {
                        process.setIsChecked(false);
                    } else {
                        process.setIsChecked(true);
                    }
                    holder.checkBox.setChecked(process.isChecked());
                }

            }
        });
    }
    /*
     *全选
     */
    public  void selectAll(View view) {
        for(ProcessEntity process:userprogress) {
            if(process.getPackageName().equals(getPackageName())){
                process.setIsChecked(false);
            }else {
                process.setIsChecked(true);
            }
        }
        for (ProcessEntity process:systemprogress) {
            if(process.getPackageName().equals(getPackageName())){
                process.setIsChecked(false);
            }else {
                process.setIsChecked(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
    /*
     *反选
     */
    public void reverseSelect(View view) {
        for(ProcessEntity process:userprogress) {
            if(process.getPackageName().equals(getPackageName())){
                process.setIsChecked(false);
            }else {
                process.setIsChecked(!process.isChecked());
            }

        }
        for(ProcessEntity process:systemprogress) {
            if(process.getPackageName().equals(getPackageName())){
                process.setIsChecked(false);
            }else {
                process.setIsChecked(!process.isChecked());
            }

        }
        adapter.notifyDataSetChanged();
    }
    /*
     *清理
     */
    public void clean(View view) {
        ActivityManager manager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ProcessEntity> list1=new ArrayList<ProcessEntity>(),list2=new ArrayList<ProcessEntity>();
        int count=0;
        long mem=0;
        for(ProcessEntity process:userprogress) {
            if(process.isChecked()) {
                count++;
                mem+=process.getMemsize();
                manager.killBackgroundProcesses(process.getPackageName());
            }else {
                list1.add(process);
            }
        }
        for(ProcessEntity process:systemprogress) {
            if(process.isChecked()) {
                count++;
                mem+=process.getMemsize();
                manager.killBackgroundProcesses(process.getPackageName());
            }else {
                list2.add(process);
            }
        }
        runningProcessNum-=count;
        availableMem+=mem;
        process_count.setText("活动进程:"+ runningProcessNum+"个");
        memory.setText("剩余/总内存:"+Formatter.formatFileSize(this, availableMem)+"/"+Formatter.formatFileSize(this,totalMem));
        userprogress=list1;
        systemprogress=list2;
        adapter.notifyDataSetChanged();
        Toast.makeText(ProcessManageActivity.this, "杀死了"+count+"个进程，共节省"+Formatter.formatFileSize(ProcessManageActivity.this,mem)+"的内存", Toast.LENGTH_SHORT).show();
    }
    public void set(View view) {
        Intent intent=new Intent(this,ProgressSettingActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
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
            if(SpUtil.getBoolean(ProcessManageActivity.this,"isShowSystemProcess",false)) {
                return userprogress.size() + systemprogress.size() + 2;
            }else {
                return userprogress.size()+1;
            }
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
                holder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                view.setTag(holder);
            }
            if(process.getPackageName().equals(getPackageName())) {
                holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setChecked(false);
            }else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(process.isChecked());
            }
            holder.image.setImageDrawable(process.getIcon());
            holder.processName.setText(process.getName());
            holder.processMem.setText("内存大小：" + Formatter.formatFileSize(ProcessManageActivity.this,process.getMemsize()));

            return view;
        }
    }
    class ViewHolder {
        ImageView image;
        TextView processName;
        TextView processMem;
        CheckBox checkBox;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}
