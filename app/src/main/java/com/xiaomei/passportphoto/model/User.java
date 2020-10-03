package com.xiaomei.passportphoto.model;

import org.json.JSONArray;

public class User {
    public String mUserID;
    public String mUserName;
    public Photo mCurrent;
    public Photo[] mPhotoList;
    public String getPhotoListString(){
        JSONArray array = new JSONArray();
        if(mPhotoList == null){
            return "";
        }
        for(Photo p:mPhotoList){
            array.put(p.toString());
        }
        return array.toString();
    }
}
