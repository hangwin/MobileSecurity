package com.study.hang.mobileSecurity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.util.SpUtil;
import com.study.hang.util.md5Util;

/*
 *   首页
 */

public class MainActivity extends Activity {
    private GridView gridView;
    private  AlertDialog dialog;
    private Button bt_ok;
    private Button bt_cancel;
    private String[] text={"手机防盗","软件管理","进程管理","缓存清理","通讯卫士","手机杀毒","流量统计","高级工具","设置中心"};
    private int[] imageId={R.drawable.lock,R.drawable.sofeware,R.drawable.process,R.drawable.clean,R.drawable.communication,
            R.drawable.virus,R.drawable.flow,R.drawable.tool,R.drawable.setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
       // AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
       // alphaAnimation.setDuration(2000);
       // findViewById(R.id.home).setAnimation(alphaAnimation);
        gridView = (GridView) findViewById(R.id.gv_home);
        gridView.setAdapter(new mAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        boolean hasSetpsd= SpUtil.getBoolean(MainActivity.this,"hasSetpsd",false);
                        if (hasSetpsd) {   //如果已经设置过密码，则输入密码后进入防盗页面；
                            inputPsdDialog();
                        }else {//如果没有设置过密码，则弹出设置密码对话框
                            SpUtil.setBoolean(MainActivity.this,"hasSetpsd",true);
                            setPsdDialog();
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setPsdDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.setpassword,null);
        bt_ok= (Button) view.findViewById(R.id.bt_ok);
        bt_cancel= (Button) view.findViewById(R.id.bt_cancel);
        final EditText text_pds= (EditText) view.findViewById(R.id.ed_psd);
        final EditText text_confirm= (EditText) view.findViewById(R.id.ed_confirmpsd);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd1=text_pds.getText().toString().trim();
                String psd2=text_confirm.getText().toString().trim();
                if(TextUtils.isEmpty(psd1)||TextUtils.isEmpty(psd2)) {
                    Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    text_pds.setText("");
                    text_confirm.setText("");
                    return;
                }
                if (!psd1.equals(psd2)) {
                    Toast.makeText(MainActivity.this,"两次输入的密码不相等",Toast.LENGTH_SHORT).show();
                    text_pds.setText("");
                    text_confirm.setText("");
                    return;
                }
                boolean res=SpUtil.setString(MainActivity.this,"userPassword", md5Util.encrypt(psd1));
                if(!res) {
                    Toast.makeText(MainActivity.this,"保存密码失败，请重试",Toast.LENGTH_SHORT).show();
                    return;
                } 
                dialog.dismiss();
                enterLock();

            }
        });

        builder.setView(view);
        dialog=builder.show();

    }




    //进入手机防盗功能
    private void enterLock() {
          Intent intent=new Intent(this,LockActivity.class);
          startActivity(intent);
    }

    private void inputPsdDialog() {
       View view= getLayoutInflater().inflate(R.layout.inputpassword,null);
       final EditText editText= (EditText) view.findViewById(R.id.ed_psd);
        bt_ok= (Button) view.findViewById(R.id.bt_ok);
        bt_cancel= (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd=editText.getText().toString().trim();
                if(TextUtils.isEmpty(psd)) {
                    Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!md5Util.encrypt(psd).equals(SpUtil.getString(MainActivity.this,"userPassword"))) {
                    Toast.makeText(MainActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    return;
                }
                dialog.dismiss();
                enterLock();
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        dialog=builder.show();
    }

    private class mAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=getLayoutInflater().inflate(R.layout.home_item,null);
            TextView tv= (TextView) view.findViewById(R.id.tv_item);
            tv.setText(text[position]);
            ImageView iv= (ImageView) view.findViewById(R.id.image);
            iv.setImageResource(imageId[position]);
            return view;
        }
    }
}
