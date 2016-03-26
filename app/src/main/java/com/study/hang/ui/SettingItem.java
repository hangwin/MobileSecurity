package com.study.hang.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.study.hang.mobileSecurity.R;

/**
 * Created by hang on 16/3/19.
 */
public class SettingItem extends RelativeLayout {
    private TextView tv_isupdate;
    private TextView tv_desc;
    public ToggleButton tg_btn;
    public SettingItem(Context context) {
        super(context);
        initView(context);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void initView(Context context) {
        View view= LayoutInflater.from(context).inflate(R.layout.setting_item,this);
        tv_isupdate= (TextView) view.findViewById(R.id.tv_isupdate);
        tv_desc= (TextView) view.findViewById(R.id.desc);
        tg_btn= (ToggleButton) view.findViewById(R.id.switch_bt);
    }

    public void setDesc(String str) {
        tv_desc.setText(str);
    }
    public void setMian(String str) { tv_isupdate.setText(str);}
    public void setIsChecked(boolean isChecked) {
        tg_btn.setChecked(isChecked);

    }

}
