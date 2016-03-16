package com.study.hang.mobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 2016/3/14.
 */
public class WelcomeActivity extends Activity {
    private ViewPager viewPager;
    private List<View> viewList;
    private int[] guideView=new int[] {R.layout.guidepage1,R.layout.guidepage2,R.layout.guidepage3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_page_layout);
        viewPager= (ViewPager) findViewById(R.id.guide_viewpager);
        viewList=new ArrayList<View>();
        for(int i=0;i<guideView.length;i++) {
            View view=getLayoutInflater().from(this).inflate(guideView[i],null);
            if(i==guideView.length-1) {
                Button button= (Button) view.findViewById(R.id.btn_enter);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            viewList.add(view);
        }
        viewPager.setAdapter(new myPagerAdapter());


    }

    private class myPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView( viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(viewList.get(position));
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
