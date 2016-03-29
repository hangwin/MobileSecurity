package com.study.hang.mobileSecurity.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
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

import com.study.hang.mobileSecurity.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hang on 16/3/26.
 */
public class AdvancedToolActivity extends Activity {
    private TextView numberbelong;
    private TextView smsbackup;
    private TextView smsrecovery;
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
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       backup();
                   }
               }).start();
            }
        });

        smsrecovery= (TextView) findViewById(R.id.smsrecovery);
        smsrecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recovery();
                    }
                }).start();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void recovery() {


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void backup() {

        try {
            ContentResolver resolver = getContentResolver();
            Uri uri = Uri.parse("content://");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "sms.xml");
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(new FileOutputStream(file), "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
            Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI, new String[]{"address", "date", "type", "body"}, null, null, null);
            serializer.attribute(null,"count",cursor.getColumnCount()+"");
            while (cursor.moveToNext()) {
                System.out.println("短信内容====》" + cursor.getString(3));
                serializer.startTag(null,"sms");
                serializer.startTag(null, "address");
                serializer.text(cursor.getString(0));
                serializer.endTag(null, "address");
                serializer.startTag(null,"date");
                serializer.text(cursor.getString(1));
                serializer.endTag(null,"date");
                serializer.startTag(null,"type");
                serializer.text(cursor.getString(2));
                serializer.endTag(null,"type");
                serializer.startTag(null,"body");
                serializer.text(cursor.getString(0));
                serializer.endTag(null,"body");
                serializer.endTag(null,"sms");

            }

            serializer.endTag(null,"smss");
            serializer.endDocument();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
