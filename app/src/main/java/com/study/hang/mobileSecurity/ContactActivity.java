package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 2016/3/23.
 */
public class ContactActivity extends Activity {
    private ListView listView;
    private List<contact_entity> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        list=new ArrayList<contact_entity>();
        listView= (ListView) findViewById(R.id.lv_contact);
        queryData();
        listView.setAdapter(new mAdapter());
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("number",list.get(position).getNumber());
                finish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void queryData() {
        ContentResolver cr=getContentResolver();
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri=Uri.parse("content://com.android.contacts/data");
        Cursor cur1=cr.query(uri, new String[]{"contact_id"}, null, null, null);
        Cursor cur2=null;
        while (cur1.moveToNext()) {
            contact_entity entity=new contact_entity();
            String id=cur1.getString(0);

            if (!TextUtils.isEmpty(id)) {
            cur2=cr.query(datauri,new String[] {"mimetype","data1"},"contact_id=?",new String[] {id},null);
             while (cur2.moveToNext()) {
                 String mime= cur2.getString(0);
                 String data=cur2.getString(1);
                 if(mime.equals("vnd.android.cursor.item/name")) {
                      entity.setName(data);
                 }else if (mime.equals("vnd.android.cursor.item/phone_v2")) {
                     entity.setNumber(data);
                 }
             }
                list.add(entity);
               // System.out.println("============>" + list.toString());
            }
        }
    }

    private class contact_entity {
        private String name;
        private String number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
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


            View view= getLayoutInflater().inflate(R.layout.contact_item,null);

            TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
            TextView tv_number= (TextView) view.findViewById(R.id.number);
            tv_name.setText(list.get(position).getName());
            tv_number.setText(list.get(position).getNumber());
            return view;
        }
    }
}
