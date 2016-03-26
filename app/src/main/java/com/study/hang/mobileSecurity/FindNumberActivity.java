package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by hang on 16/3/26.
 */
public class FindNumberActivity extends Activity {
    private EditText et_number;
    private Button bt_find;
    private TextView tv_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findnumber);
        et_number= (EditText) findViewById(R.id.et_number);
        bt_find= (Button) findViewById(R.id.bt_find);
        tv_show= (TextView) findViewById(R.id.tv_show);
        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
