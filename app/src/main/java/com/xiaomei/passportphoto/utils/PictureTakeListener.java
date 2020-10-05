package com.xiaomei.passportphoto.utils;

import android.graphics.Bitmap;

public interface PictureTakeListener{
    void onPictureTake(Bitmap bmp, String imagePath);
}