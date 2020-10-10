package com.xiaomei.passportphoto.logic;

import android.os.Handler;

import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;

import java.io.Serializable;
import java.util.Map;

public class PhotoController {

    private static PhotoController mInstance = null;
    public PhotoController(){
    }

    public static PhotoController getInstance(){
        if(mInstance == null){
            mInstance = new PhotoController();
        }
        return mInstance;
    }

    public boolean sessionIsValid(){
        long updatetime = RunContext.getInstance().mUpdateSessionTime;
        if(updatetime == 0 || System.currentTimeMillis() - updatetime > 24*3600*1000){
            return false;
        }
        return true;
    }

    public void sendLoginRequest(Handler handle, Runnable run){
        LoginRequest lr = new LoginRequest(handle,run);
        Map<String, Serializable> params = lr.mParams;
        params.put("cmd","login");
        params.put("loginid", RunContext.getInstance().getUser().mUserID);
        params.put("userver",RunContext.getInstance().mVer);
        params.put("usermodel",RunContext.getInstance().mModel);
        params.put("useros",RunContext.getInstance().mOS);
        params.put("photoidlist",RunContext.getInstance().getUser().getPhotoListString());
        lr.doRequest();
    }

    public void sendMattingRequest(Photo photo, Handler handle, Runnable run){
        MattingRequest mr = new MattingRequest(handle,run);
        Map<String, Serializable> params = mr.mParams;
        params.put("cmd","matting");
        params.put("loginid",RunContext.getInstance().getUser().mUserID);
        params.put("session",RunContext.getInstance().mSession);
        params.put("photoorigin", photo.mNetForMatting_PhotoOrigin);
        mr.doRequest();
    }

    public void sendUploadRequest(Photo photo, Handler handle, Runnable run){
        UploadRequest ur = new UploadRequest(handle,run);
        Map<String, Serializable> params = ur.mParams;
        params.put("cmd","upload");
        User user = RunContext.getInstance().getUser();
        params.put("loginid",RunContext.getInstance().getUser().mUserID);
        params.put("session",RunContext.getInstance().mSession);
        params.put("photopost",photo.mNetForUpload_PhotoPost);
        params.put("thumbnail",photo.mNetForUpload_PhotoThumbnail);
        params.put("photoid",photo.mPID);
        params.put("spec", RunContext.getInstance().mSpecType);
        params.put("w",RunContext.getInstance().mSpec.mSizeW);
        params.put("h",RunContext.getInstance().mSpec.mSizeH);
        params.put("bg",RunContext.getInstance().mBackground);
        ur.doRequest();
    }

    public ImageRequest getPhotoByUrl(String url, Handler handle, Runnable run){
        ImageRequest ir = new ImageRequest(url,handle,run);
        ir.doRequest();
        return ir;
    }
}
