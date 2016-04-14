package com.study.hang.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by hang on 16/4/14.
 */
public class FaceUtil {
    public interface CallBack {
        public void Success(JSONObject result);

        public void Fail(String info);
    }

    public static void detect(final Bitmap bm,final CallBack callBack, final boolean bigFace) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequests request = new HttpRequests(FaceConstant.Key,FaceConstant.Secret,true,true);
                    PostParameters param = new PostParameters();
                    if(bigFace) param.setMode("oneface");
                    Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] data = stream.toByteArray();
                    param.setImg(data);
                    JSONObject jsonObject=request.detectionDetect(param);
                    Log.i("=====JSON====",jsonObject.toString());
                    callBack.Success(jsonObject);
                }
                catch (FaceppParseException e) {
                    callBack.Fail(e.getErrorMessage());
                }
            }
        }).start();
    }
}



