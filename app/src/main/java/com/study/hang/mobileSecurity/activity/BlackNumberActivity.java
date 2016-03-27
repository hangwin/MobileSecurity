package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.db.BlackNumberDao;
import com.study.hang.db.BlackNumberEntity;
import com.study.hang.mobileSecurity.R;

import java.util.List;

/**
 * Created by hang on 16/3/27.
 */
public class BlackNumberActivity extends Activity {
    private ListView lv;
    private Button bt_add;
    private Dialog dialog;
    private BlackNumberDao dao;
    private myAdapter adapter;
    private List<BlackNumberEntity> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_number);
        dao=new BlackNumberDao(this);
        list=dao.queryAll();
        lv= (ListView) findViewById(R.id.lv);
        adapter=new myAdapter();
        lv.setAdapter(adapter);
       // System.out.println("=============size=======>"+list.size()+"===>"+list.toString());
        bt_add= (Button) findViewById(R.id.bt_add);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(BlackNumberActivity.this);
                View view=getLayoutInflater().inflate(R.layout.addblacknumber,null);
                final EditText et_number= (EditText) view.findViewById(R.id.et_number);
                final CheckBox c1= (CheckBox) view.findViewById(R.id.call);
                final CheckBox c2= (CheckBox) view.findViewById(R.id.message);
                Button bt_ok= (Button) view.findViewById(R.id.bt_ok);
                Button bt_cancel= (Button) view.findViewById(R.id.bt_cancel);
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number=et_number.getText().toString().trim();
                        String mode="3";
                        if(!c1.isChecked()&&!c2.isChecked()) {
                            Toast.makeText(BlackNumberActivity.this,"请选择拦截模式",Toast.LENGTH_SHORT).show();
                            return;
                        }else if(c1.isChecked()&&c2.isChecked()) {
                            mode="3";
                        } else if(c1.isChecked()) {
                            mode="1";
                        }else if(c2.isChecked()) {
                            mode="2";
                        }
                        dao.insert(number, mode);
                        list.add(new BlackNumberEntity(mode, number));
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();

                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                builder.setView(view);
               dialog=builder.show();
            }
        });

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            viewHolder holder;
            if(convertView==null) {
                holder=new viewHolder();
                view=getLayoutInflater().inflate(R.layout.blacknumber_item,null);
                holder.black_number=(TextView) view.findViewById(R.id.tv_blacknumber);
                holder.mode=(TextView) view.findViewById(R.id.mode);
                holder.del=(ImageView) view.findViewById(R.id.delete);

                view.setTag(holder);
            }else {
                view=convertView;
                holder= (viewHolder) view.getTag();
            }
            holder.black_number.setText(list.get(position).getNumber());
           // System.out.println("holder.getText====>" + holder.black_number.getText().toString());
            String m=list.get(position).getMode();

            if("1".equals(m)) {
                m="拦截电话";
            }else if("2".equals(m)) {
                m="拦截短信";
            }else if("3".equals(m)) {
                m="拦截所有";
            }
           // System.out.println("=======>"+m);
            holder.mode.setText(m);
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       dao.delete(list.get(position).getNumber());
                       list.remove(position);
                       adapter.notifyDataSetChanged();
                }
            });
            return view;
        }
        class viewHolder{
            TextView black_number;
            TextView mode;
            ImageView del;
        }
    }
}
