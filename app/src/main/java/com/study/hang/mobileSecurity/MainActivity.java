package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/*
 *   首页
 */

public class MainActivity extends Activity {
    private GridView gridView;
    private String[] text={"手机防盗","软件管理","进程管理","缓存清理","通讯卫士","手机杀毒","流量统计","高级工具","设置中心"};
    private int[] imageId={R.drawable.lock,R.drawable.sofeware,R.drawable.process,R.drawable.clean,R.drawable.communication,
            R.drawable.virus,R.drawable.flow,R.drawable.tool,R.drawable.setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(2000);
        findViewById(R.id.home).setAnimation(alphaAnimation);
        gridView = (GridView) findViewById(R.id.gv_home);
        gridView.setAdapter(new mAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
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
