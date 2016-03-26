package com.study.hang.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.study.hang.mobileSecurity.R;

/**
 * Created by hang on 16/3/19.
 */
public class ClickItem extends RelativeLayout {
    private TextView title;
    private TextView tv_desc;
    public ImageView image;
    public ClickItem(Context context) {
        super(context);
        initView(context);
    }

    public ClickItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ClickItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClickItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void initView(Context context) {
        View view= LayoutInflater.from(context).inflate(R.layout.click_item,this);
        title= (TextView) view.findViewById(R.id.tv_title);
        tv_desc= (TextView) view.findViewById(R.id.desc);
        image= (ImageView) view.findViewById(R.id.image);
    }

    public void setDesc(String str) {
        tv_desc.setText(str);
    }
    public void setMian(String str) { title.setText(str);}
   

}
