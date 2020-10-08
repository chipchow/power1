package com.xiaomei.passportphoto.model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String mUserID;
    public String mUserName;
    public Photo mCurrent;
    public List<Photo> mPhotoList;

    public User(){
        mPhotoList = new ArrayList<Photo>();
    }

    public void setmCurrent(Photo current){
        mCurrent = current;
    }

    public List<Photo> getmPhotoList(){
        return mPhotoList;
    }

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
