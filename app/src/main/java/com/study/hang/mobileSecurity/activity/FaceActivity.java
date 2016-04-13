package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.study.hang.mobileSecurity.R;

import java.net.URI;

/**
 * Created by hang on 2016/4/13.
 */
public class FaceActivity extends Activity {
    private static final int PIC = 1;
    private Button load;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face);
        load= (Button) findViewById(R.id.load);
        img= (ImageView) findViewById(R.id.img);
    }

    public void loadImg(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PIC);
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
                img.setImageBitmap(resizePhoto(path));

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
