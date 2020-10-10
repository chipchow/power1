package com.xiaomei.passportphoto.logic;

import android.os.Handler;

import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.PhotoSpec;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
            RunContext.getInstance().mCost = mJson.getInt("price");
            JSONArray photolist = mJson.getJSONArray("photolist");
            int listsize = photolist.length();
            if(listsize > 0) {
                List<Photo> photoList = user.getmPhotoList();

                for (int i = 0; i < photolist.length(); i++) {
                    JSONObject o = photolist.getJSONObject(i);
                    String pid = o.getString("pid");
                    Photo p = new Photo(pid,0);
                    p.mThumbnailPath =o.getString("thumbnail");
                    p.mPostPath=o.getString("p");
                    p.fixUrl();
                    p.mPaid = true;
                    p.mBackGround = o.getInt("bg");
                    p.mSpecType = o.getInt("spec");
                    p.mSpec = new PhotoSpec(p.mSpecType);
                    photoList.add(p);
                }
                RunContext.getInstance().save(PhotoApp.getAppContext());
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
