package com.xiaomei.passportphoto.logic;

import android.os.Handler;
import android.util.Base64;

import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.RunContext;

import org.json.JSONException;

public class MattingRequest extends BaseHttpPost {

    public MattingRequest(Handler handle, Runnable run){
        super(handle,run);
    }
    public void parseBody() {
        super.parseBody();

        if(mJson == null){
            return;
        }

        try {
            byte[] protomat = mJson.getString("photomat").getBytes();
            String photoid = mJson.getString("photoid");
            byte[] image = Base64.decode(protomat, Base64.DEFAULT);
            Photo p = RunContext.getInstance().mUser.mCurrent;
            if(p != null){
                p.mPID = photoid;
                p.mNetForMatting_PhotoMat = image;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
}
