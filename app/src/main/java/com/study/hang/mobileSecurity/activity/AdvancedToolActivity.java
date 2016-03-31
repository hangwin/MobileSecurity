package com.study.hang.mobileSecurity.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Telephony;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.db.smsEntity;
import com.study.hang.mobileSecurity.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/3/26.
 */
public class AdvancedToolActivity extends Activity {
    private TextView numberbelong;
    private TextView smsbackup;
    private TextView smsrecovery;
    private ProgressDialog progressDialog;
    private List<smsEntity> list;
    private AlertDialog dialog;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancedtool);
        numberbelong= (TextView) findViewById(R.id.numberbelong);
        numberbelong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdvancedToolActivity.this,FindNumberActivity.class);
                startActivity(intent);
            }
        });

        smsbackup= (TextView) findViewById(R.id.smsbackup);
        smsbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(AdvancedToolActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("备份中...");
                progressDialog.show();

                new Thread(new Runnable() {
                   @Override
                   public void run() {
                       backup(progressDialog);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(AdvancedToolActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
                               progressDialog.dismiss();
                           }
                       });
                   }
               }).start();

            }
        });

        smsrecovery= (TextView) findViewById(R.id.smsrecovery);
        smsrecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(AdvancedToolActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("恢复中...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("before...");
                        recovery(progressDialog);
                        System.out.println("====》" + list);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(AdvancedToolActivity.this, "恢复成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void recovery(ProgressDialog progressDialog) {
        try {
            final ContentResolver resolver = getContentResolver();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder=new AlertDialog.Builder(AdvancedToolActivity.this);
                    builder.setMessage("恢复备份之前是否先删除已有短信");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resolver.delete(Telephony.Sms.CONTENT_URI,null,null);
                            dialog.dismiss();
                        }
                    });
                    dialog=builder.show();
                }
            });
           /* XmlPullParser parser = Xml.newPullParser();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "sms.xml");
            parser.setInput(new FileInputStream(file), "utf-8");
            int eventType= parser.getEventType();
            smsEntity sms=null;
            int num=0;
            while (eventType!=XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("smss".equals(parser.getName())) {
                            progressDialog.setMax( Integer.parseInt(parser.getAttributeValue(null, "count")));
                            list=new ArrayList<smsEntity>();

                        }else if("sms".equals(parser.getName())) {
                            sms=new smsEntity();

                        }else if("address".equals(parser.getName())) {
                            sms.setAddress(parser.nextText());

                        }else if("date".equals(parser.getName())) {
                            sms.setDate(parser.nextText());

                        }
                        else if("type".equals(parser.getName())) {
                            sms.setType(parser.nextText());

                        }
                        else if("body".equals(parser.getName())) {

                            sms.setBody(parser.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if("sms".equals(parser.getName())) {
                            System.out.println("====>"+parser.getName());
                            list.add(sms);
                        }
                        break;
                }
                eventType=parser.next();
                progressDialog.setProgress(num++);
            }

            for(smsEntity entity:list) {
                ContentValues values=new ContentValues();
                values.put("address",entity.getAddress());
                values.put("date",entity.getDate());
                values.put("type",entity.getType());
                values.put("body",entity.getBody());
                resolver.insert(Telephony.Sms.CONTENT_URI,values);
            }*/

        }catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AdvancedToolActivity.this,"恢复失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void backup(ProgressDialog progressDialog) {

        try {
            ContentResolver resolver = getContentResolver();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "sms.xml");
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(new FileOutputStream(file), "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
            Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI, new String[]{"address", "date", "type", "body"}, null, null, null);
            serializer.attribute(null, "count", cursor.getColumnCount() + "");
            progressDialog.setMax(cursor.getColumnCount());
            int current=0;
            while (cursor.moveToNext()) {
                Thread.sleep(1000);
                progressDialog.setProgress(current++);
                System.out.println("短信内容====》" + cursor.getString(3));
                serializer.startTag(null, "sms");
                serializer.startTag(null, "address");
                serializer.text(cursor.getString(0));
                serializer.endTag(null, "address");
                serializer.startTag(null, "date");
                serializer.text(cursor.getString(1));
                serializer.endTag(null, "date");
                serializer.startTag(null, "type");
                serializer.text(cursor.getString(2));
                serializer.endTag(null, "type");
                serializer.startTag(null, "body");
                serializer.text(cursor.getString(0));
                serializer.endTag(null, "body");
                serializer.endTag(null,"sms");

            }

            serializer.endTag(null,"smss");
            serializer.endDocument();
        }catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AdvancedToolActivity.this, "备份失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
