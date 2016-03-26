package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.study.hang.mobileSecurity.R;

/**
 * Created by hang on 16/3/26.
 */
public class AdvancedToolActivity extends Activity {
    private TextView numberbelong;
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
    }
}
