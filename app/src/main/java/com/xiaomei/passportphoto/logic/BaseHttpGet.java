package com.xiaomei.passportphoto.logic;

import android.os.Handler;

import com.baidu.aip.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

public class BaseHttpGet implements Runnable{
    public final static String URLPREFIX = "http://47.93.123.181/xiaomei/";
    HttpURLConnection mHttpClient = null;
    ThreadPool mTheadPool = ThreadPool.getInstance();
    protected String mURL;
    File mCaheFile;
    Handler mHandler;
    Runnable mRun;

    public BaseHttpGet(String url, Handler handle,Runnable run){
        mHandler = handle;
        mRun = run;
        mURL = URLPREFIX+url;
        String cache = PhotoApp.getAppContext().getFilesDir()+ File.separator+getKey();
        mCaheFile = new File(cache);
    }


    public String getKey(){
        return Util.uriEncode(mURL,true);
    }

    public void run(){

        try {
            URL url = new URL(mURL);
            mHttpClient = (HttpURLConnection) url.openConnection();
            mHttpClient.connect();
            /* 取得返回的InputStream */
            InputStream is = mHttpClient.getInputStream();
            //得到网络文件大小
            long size = mHttpClient.getContentLength();

            FileOutputStream fops = new FileOutputStream(mCaheFile);
            byte[] buffer = new byte[1024 * 8];
            int length = 0;
            int readed = 0;
            while((length=is.read(buffer))!=-1){
                fops.write(buffer);
                fops.flush();
                readed += length;
            }
            /* 关闭InputStream */
            fops.close();
            is.close();
            mHandler.post(mRun);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void doRequest(){
        Future<?> future = mTheadPool.mExecutor.submit(this);

    }
}
