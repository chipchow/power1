package com.xiaomei.passportphoto.model;

public class PhotoSpec {
    public int mWidth;
    public int mHeight;
    public int mSizeW;
    public int mSizeH;
    public int mSmallest;
    public int mBigest;
    public float mDPI;
    public PhotoSpec(int width, int height, int sizew,int sizeh, int smallest, int bigest, float dpi){
        mWidth = width;
        mHeight = height;
        mSizeW = sizew;
        mSizeH = sizeh;
        mSmallest = smallest;
        mBigest = bigest;
        mDPI = dpi;
    }
}
