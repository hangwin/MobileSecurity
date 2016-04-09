package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;

import com.study.hang.mobileSecurity.R;

/**
 * Created by hang on 16/4/9.
 */
public class LockAppActivity extends Activity {
    private EditText psd;
    private Button ok;
    private Button cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_app);
        psd= (EditText) findViewById(R.id.ed_psd);
        ok= (Button) findViewById(R.id.ok);
        cancle= (Button) findViewById(R.id.cancle);
        System.out.println("=======>lockAppActivity");
    }
}
