package com.xiaomei.passportphoto.logic;

import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.utils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import android.os.Handler;
import android.util.Log;

public class BaseHttpPost implements Runnable{
    Map<String, Serializable> mParams = new HashMap<String,Serializable>();
    Map<String, Serializable> mRet = new HashMap<String,Serializable>();
    public final static String mURL = "http://47.93.123.181/xiaomei/index.php?";
    HttpURLConnection mHttpClient = null;
    ThreadPool mTheadPool = ThreadPool.getInstance();
    String mBodyString;
    JSONObject mJson;
    Handler mHandler;
    Runnable mRun;

    public BaseHttpPost(Handler handle,Runnable run){
        mHandler = handle;
        mRun = run;
    }

    public String getSign(String params){
        return BitmapUtils.md5(params);
    }

    public void run(){

        try{
            URL url = new URL(mURL);
            mHttpClient = (HttpURLConnection) url.openConnection();
            mHttpClient.setDoOutput(true);     //需要输出
            mHttpClient.setDoInput(true);      //需要输入
            mHttpClient.setUseCaches(false);   //不允许缓存
            mHttpClient.setRequestMethod("POST");
            mHttpClient.setConnectTimeout(3000);
            mHttpClient.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            mHttpClient.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            mHttpClient.setRequestProperty("Charset", "UTF-8");
            mHttpClient.setRequestProperty("Accept", "application/json");
            mHttpClient.connect();

            StringBuffer buffer = new StringBuffer();
            if(mParams != null&&!mParams.isEmpty()){
                //迭代器
                //Map.Entry 是Map中的一个接口，他的用途是表示一个映射项（里面有Key和Value）
                for(Map.Entry<String, Serializable> entry : mParams.entrySet()){
                    if(entry.getValue() instanceof byte[]) {
                        String value = new String((byte[])entry.getValue(),"UTF-8");
                        buffer.append(entry.getKey()).append("=").append(value) .append("&");
                    }else{
                        Log.d("basehttppost",entry.getKey()+":"+entry.getValue());
                        buffer.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
                    }
                }
            }
            String sign = getSign(buffer.substring(0,buffer.length()-2));
            buffer.append("sign="+sign);
            OutputStream outputStream = mHttpClient.getOutputStream();
            outputStream.write(buffer.toString().getBytes(),0, buffer.length());


            int resultCode=mHttpClient.getResponseCode();
            if(HttpURLConnection.HTTP_OK==resultCode){
                mBodyString = changeInputeStream(mHttpClient.getInputStream(),"utf-8");
            }
            parseBody();
            mHandler.post(mRun);
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String changeInputeStream(InputStream inputStream, String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if(inputStream != null){
            try {
                while((len = inputStream.read(data))!=-1){
                    data.toString();

                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(),encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }


    public void parseBody(){
        try {
            JSONObject obj = new JSONObject(mBodyString);
            int error = obj.getInt("error");
            switch(error){
                case 0:
                    mJson = obj.getJSONObject("resp");
                    break;
                case 1://version error
                    break;
                case 2://session valid
                    Runnable logincb = new Runnable() {
                        @Override
                        public void run() {
                            doRequest();//request again
                        }
                    };
                    LoginRequest lr = new LoginRequest(mHandler,logincb);
                    Map<String, Serializable> params = lr.mParams;
                    params.put("loginid", RunContext.getInstance().getUser().mUserID);
                    params.put("userver",RunContext.getInstance().mVer);
                    params.put("usermodel",RunContext.getInstance().mModel);
                    params.put("useros",RunContext.getInstance().mOS);
                    params.put("photoidlist",RunContext.getInstance().getUser().getPhotoListString());
                    lr.doRequest();
                    break;
                case 3://sign error
                    break;
                case 4://parameter error
                    break;
                case 5:// unknow cmd
                    break;
                default:
                    break;
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void doRequest(){
        Future<?> future = mTheadPool.mExecutor.submit(this);

    }


}
