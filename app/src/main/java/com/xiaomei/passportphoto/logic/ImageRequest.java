package com.xiaomei.passportphoto.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.util.concurrent.Future;

public class ImageRequest extends BaseHttpGet {
    public Bitmap mBitmap;
    public ImageRequest(String url, Handler handle, Runnable run){
        super(url,handle,run);
    }


    public boolean isExist(){
        if(mCaheFile.exists()){
            return true;
        }

        return false;
    }

    public class DecodeFile implements Runnable{
        public void run(){
            mBitmap = BitmapFactory.decodeFile(mCaheFile.toString());
            mHandler.post(mRun);
        }
    }
    public void doRequest() {
        if(isExist()){
            Future<?> future = mTheadPool.mExecutor.submit(new DecodeFile());
        }else{
            super.doRequest();
        }
    }
}
