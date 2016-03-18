package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.2f,1.0f);
        alphaAnimation.setDuration(2000);
        findViewById(R.id.home).setAnimation(alphaAnimation);
    }


}
