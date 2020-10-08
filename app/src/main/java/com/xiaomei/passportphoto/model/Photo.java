package com.xiaomei.passportphoto.model;

import java.io.Serializable;

public class Photo implements Serializable {
    public String mPID;
    public String mThumbnail;
    public byte[] mDownloadThumbnail;
    public String mPhotoOrigin;
    public String mPhotoPost;
    public byte[] mPhotoMat;
    @Override
    public String toString() {
        return mPID;
    }
}
