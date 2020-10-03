package com.xiaomei.passportphoto.model;

import java.io.Serializable;

public class Photo implements Serializable {
    public String mPID;
    public String mThumbPath;
    public byte[] mThumbnail;
    public String mPhotoOrigin;
    public byte[] mPhotoPost;
    public byte[] mPhotoMat;
    @Override
    public String toString() {
        return mPID;
    }
}
