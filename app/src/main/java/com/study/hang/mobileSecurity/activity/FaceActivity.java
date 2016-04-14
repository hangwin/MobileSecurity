package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hang.mobileSecurity.R;
import com.study.hang.util.FaceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by hang on 2016/4/13.
 */
public class FaceActivity extends Activity {
    private static final int PIC = 1;
    private static final int SUCCESS = 2;
    private static final int FAIL = 3;
    private static final int BIFFACE = 4;
    private Button load;
    private ImageView img;
    private Bitmap bitmap;
    private FrameLayout pb;
    private TextView info;
    private Paint paint;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what){
                case SUCCESS:
                    JSONObject jsonObject= (JSONObject) msg.obj;
                    getnewImage(jsonObject,false);
                    img.setImageBitmap(bitmap);
                    break;
                case FAIL:
                    break;
                case BIFFACE:
                    JSONObject json= (JSONObject) msg.obj;
                    getnewImage(json,true);
                    img.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    private void getnewImage(JSONObject jsonObject,boolean bigface) {
        Bitmap newbitmap=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        Canvas canvas=new Canvas(newbitmap);
        canvas.drawBitmap(bitmap,0,0,paint);
        try {
            JSONArray faces=jsonObject.getJSONArray("face");
            info.setText("发现了"+faces.length()+"张脸");
            for(int i=0;i<faces.length();i++) {
                JSONObject face= (JSONObject) faces.get(i);
                JSONObject position=face.getJSONObject("position");
                JSONObject center=position.getJSONObject("center");
                float x= (float) center.getDouble("x");
                float y= (float) center.getDouble("y");
                float width= (float) position.getDouble("width");
                float height= (float) position.getDouble("height");
                x=x/100*newbitmap.getWidth();
                y=y/100*newbitmap.getHeight();
                width=width/100*newbitmap.getWidth();
                height=height/100*newbitmap.getHeight();
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(4);
                canvas.drawLine(x - width / 2, y - height / 2, x - width / 2, y - height / 2 + height, paint);
                canvas.drawLine(x - width / 2, y - height / 2, x - width / 2 + width, y - height / 2, paint);
                canvas.drawLine(x-width/2+width,y-height/2,x-width/2+width,y-height/2+height,paint);
                canvas.drawLine(x - width / 2, y - height / 2 + height, x - width / 2 + width, y - height / 2 + height, paint);

                String gender=face.getJSONObject("attribute").getJSONObject("gender").getString("value");
                int age=face.getJSONObject("attribute").getJSONObject("age").getInt("value");
                Bitmap ageBitmap=getAgeBitmap(age, gender,bigface);
                int agewidth= ageBitmap.getWidth();
                int ageHeight=ageBitmap.getHeight();
                if(newbitmap.getWidth()<bitmap.getWidth()&&newbitmap.getHeight()<bitmap.getHeight()) {
                    int radio=Math.max((int) newbitmap.getWidth()/bitmap.getWidth(),(int) bitmap.getHeight()/bitmap.getHeight());
                    ageBitmap=Bitmap.createScaledBitmap(ageBitmap,agewidth*radio,ageHeight*radio,false);
                    System.out.println("=========>in++++++++++");
                }
               // ageBitmap=Bitmap.createScaledBitmap(ageBitmap,(int) (agewidth*0.8),(int) (ageHeight*0.4),false);
                canvas.drawBitmap(ageBitmap,x-agewidth/2,y-height/2-ageHeight,paint);
                bitmap=newbitmap;

            }

        } catch (JSONException e) {
            info.setText("解析出错,请重试");
            e.printStackTrace();
        }
    }

    private Bitmap getAgeBitmap(int age, String gender,boolean bigface) {
        TextView info= (TextView) pb.findViewById(R.id.showinfo);
        if(bigface) {

            info.setText("大");
            this.info.setText("经过科学的计算，圈中的人脸最大");

        }else {
            info.setText(age + "");
        }
        if("Female".equals(gender)) {
            info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female),null,null,null);
        }else {
            info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male),null,null,null);
        }
        info.setDrawingCacheEnabled(true);
        Bitmap bitmap=Bitmap.createBitmap(info.getDrawingCache());
        info.destroyDrawingCache();
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face);
        load= (Button) findViewById(R.id.load);
        img= (ImageView) findViewById(R.id.img);
        pb= (FrameLayout) findViewById(R.id.pb);
        info= (TextView) findViewById(R.id.info);
        paint=new Paint();
    }

    public void loadImg(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PIC);
    }

    public void detect(View view) {
        if (bitmap==null) {
            bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.t4);
        }
        pb.setVisibility(View.VISIBLE);
        FaceUtil.detect(bitmap, new FaceUtil.CallBack() {
            @Override
            public void Success(JSONObject result) {
                Message message = Message.obtain();
                message.what = SUCCESS;
                message.obj = result;
                handler.sendMessage(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void Fail(String info) {
                Message message = Message.obtain();
                message.what = FAIL;
                message.obj = info;
                handler.sendMessage(message);
            }
        },false);

    }

    public void bigFace(View view) {
        if (bitmap==null) {
            bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.t4);
        }
        pb.setVisibility(View.VISIBLE);
        FaceUtil.detect(bitmap, new FaceUtil.CallBack() {
            @Override
            public void Success(JSONObject result) {
                Message message = Message.obtain();
                message.what = BIFFACE;
                message.obj = result;
                handler.sendMessage(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void Fail(String info) {
                Message message = Message.obtain();
                message.what = FAIL;
                message.obj = info;
                handler.sendMessage(message);
            }
        },true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PIC) {
            if(data!=null) {
                Uri uri=data.getData();
                Cursor cursor=getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int index=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String path=cursor.getString(index);
                cursor.close();
                bitmap=resizePhoto(path);
                img.setImageBitmap(bitmap);

            }
        }
    }

    public Bitmap resizePhoto(String path) {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        double radio=Math.max(options.outWidth*1.0/1024,options.outHeight*1.0/1024);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path, options);
    }
}
