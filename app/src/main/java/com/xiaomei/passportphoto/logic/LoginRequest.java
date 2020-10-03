package com.xiaomei.passportphoto.logic;

import android.os.Handler;
import android.util.Base64;

import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends BaseHttpPost {

    public LoginRequest(Handler handle, Runnable run){
        super(handle,run);
    }
    public void parseBody(){
        super.parseBody();
        if(mJson == null){
            return;
        }
        User user = RunContext.getInstance().getUser();
        try {
            String session = mJson.getString("session");
            RunContext.getInstance().setSession(session);
            String updateUrl = mJson.getString("updateapk");
            JSONArray photolist = mJson.getJSONArray("photolist");
            int listsize = photolist.length();
            if(listsize > 0) {
                user.mPhotoList = new Photo[listsize];
                for (int i = 0; i < photolist.length(); i++) {
                    JSONObject o = photolist.getJSONObject(i);
                    user.mPhotoList[i].mPID = o.getString("photoid");
                    byte[] thumb = o.getString("thumbnail").getBytes();
                    user.mPhotoList[i].mThumbnail = Base64.decode(thumb, Base64.DEFAULT);
                    user.mPhotoList[i].mThumbPath = "";
                }
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
