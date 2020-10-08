package com.xiaomei.passportphoto.logic;

import android.os.Handler;
import android.util.Base64;

import com.xiaomei.passportphoto.model.Photo;
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
            JSONArray photolist = mJson.getJSONArray("photolist");
            int listsize = photolist.length();
            if(listsize > 0) {
                List<Photo> photoList = user.getmPhotoList();

                for (int i = 0; i < photolist.length(); i++) {
                    JSONObject o = photolist.getJSONObject(i);
                    String pid = o.getString("photoid");
                    Photo p = new Photo(pid,0);
                    byte[] thumb = o.getString("thumbnail").getBytes();
                    byte[] bytes = Base64.decode(thumb, Base64.DEFAULT);
                    p.setNetForLoginThumbnail(PhotoApp.getAppContext(),bytes,pid);
                    photoList.add(p);
                }
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
