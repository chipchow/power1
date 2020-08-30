package com.xiaomei.passportphoto.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.xiaomei.passportphoto.activity.CropImageActivity;
import com.xiaomei.passportphoto.activity.RotationImageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import static java.lang.System.in;

public class MyAsyncTask extends AsyncTask<Bitmap, Void, Void> {
    private ProgressDialog dialog;
    private RotationImageActivity activity;

    public MyAsyncTask(RotationImageActivity activity) {
        dialog = new ProgressDialog(activity);
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("处理中，请稍候.");
        dialog.setCancelable(false);
        dialog.show();
    }


    public static final String APP_ID = "22356022";
    public static final String API_KEY = "gAmhhkQ1aROQXkk3ol1DiWl0";
    public static final String SECRET_KEY = "Fo6QF8qByw6ww6cgUr2Q6CuP6yVZCYok";
    public  static AipBodyAnalysis mClient = null;
    public static final String TAG = "xiaomei";
    public static AipBodyAnalysis getipBodyAnalysisInstance() {
        // 初始化一个AipBodyAna
        if(mClient != null){
            return mClient;
        }
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }


    private byte[] readFileToByteArray(String path) {
        File file = new File(path);
        if(!file.exists()) {
            Log.e(TAG,"File doesn't exist!");
            return null;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            long inSize = in.getChannel().size();//判断FileInputStream中是否有内容
            if (inSize == 0) {
                Log.d(TAG,"The FileInputStream has no content!");
                return null;
            }

            byte[] buffer = new byte[in.available()];//in.available() 表示要读取的文件中的数据长度
            in.read(buffer);  //将文件中的数据读到buffer中
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                return null;
            }
            //或IoUtils.closeQuietly(in);
        }
    }

    public static void saveByteArray(byte[] bytes, String filepath){
        try {
            File file = new File(filepath);
            String dir = file.getParent();
            File dirAsFile = new File(dir);
            if(!dirAsFile.exists()) {
                Log.e(TAG,"File doesn't exist!");
                dirAsFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bitmap, String filepath){
        try {
            File file = new File(filepath);
            String dir = file.getParent();
            File dirAsFile = new File(dir);
            if(!dirAsFile.exists()) {
                Log.e(TAG,"File doesn't exist!");
                dirAsFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void bodyseg(AipBodyAnalysis client, String filename) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("type", "foreground");
        // 参数为二进制数组
        try {
            byte[] file = readFileToByteArray(filename);
            JSONObject res = client.bodySeg(file, options);
            String fg = (String)res.get("foreground");
            System.out.println(res.toString(2));
            byte[] bytes = Base64.getDecoder().decode(fg);
            saveByteArray(bytes,RotationImageActivity.filename2);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        try {
            saveBitmap(bitmaps[0],RotationImageActivity.filename);

//            bitmaps[0].recycle();
            bodyseg(getipBodyAnalysisInstance(),RotationImageActivity.filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        activity.startActivity(new Intent(activity, CropImageActivity.class));
    }
}
