package com.xiaomei.passportphoto.model;

public class PhotoSpec {
    public int mWidth;
    public int mHeight;
    public int mSmallest;
    public int mBigest;
    public int mDPI;
    public PhotoSpec(int width, int height, int smallest, int bigest, int dpi){
        mWidth = width;
        mHeight = height;
        mSmallest = smallest;
        mBigest = bigest;
        mDPI = dpi;
    }
}
