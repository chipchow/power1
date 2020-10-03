package com.xiaomei.passportphoto.logic;

import android.os.Handler;

import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;

public class UploadRequest extends BaseHttpPost {
    public UploadRequest(Handler handle, Runnable run){
        super(handle,run);
    }

    public void parseBody() {
        super.parseBody();

        if(mJson == null){
            return;
        }
    }
}
