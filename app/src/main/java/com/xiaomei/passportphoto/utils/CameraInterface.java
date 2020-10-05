package com.xiaomei.passportphoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.xiaomei.passportphoto.activity.TakeActivity;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CameraInterface {
    Camera mCamera;
    Camera.Parameters mCameraParamters;
    Camera.PreviewCallback mCameraPreviewCallback = new CameraPreviewCallback();
    byte[] mImageCallbackBuffer;
    SurfaceHolder mSurfaceHolder;
    int m_W,m_H;
    static private SurfaceTexture mSurfaceTexture = new SurfaceTexture(11);

    public CameraInterface(SurfaceHolder surfaceHolder){
        mSurfaceHolder = surfaceHolder;
    }
    public void stopCamera(){
        if (null == mCamera) {
            return;
        }
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void startCamera(int cameraId){
        try {
            mCamera = Camera.open(cameraId);
            mCamera.setDisplayOrientation(90);
            mCameraParamters = mCamera.getParameters();
            mCameraParamters.setPreviewFormat(ImageFormat.YV12);
            mCameraParamters.setFlashMode("off");
            List<String> focusModes = mCameraParamters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mCameraParamters
                        .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            List<Camera.Size> ls = mCameraParamters.getSupportedPreviewSizes();
            m_W = ls.get(0).width;
            m_H = ls.get(0).height;
            int delta = Math.abs(m_W-1280);
            Iterator<Camera.Size> iter = ls.iterator();
            while(iter.hasNext()) {
                Camera.Size sz = iter.next();
                if (Math.abs(sz.width - 1280) < delta) {
                    m_W = sz.width;
                    m_H = sz.height;
                    delta = Math.abs(sz.width - 1280);
                }
            }
            mCameraParamters.setPreviewSize(m_W,m_H);
            mCameraParamters.setPictureSize(m_W,m_H);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                mCameraParamters.setRecordingHint(true);
//            }
            mCamera.setParameters(mCameraParamters);

            mCameraParamters = mCamera.getParameters();
            int size = mCameraParamters.getPreviewSize().width * mCameraParamters.getPreviewSize().height;
            size = (size * ImageFormat.getBitsPerPixel(mCameraParamters.getPreviewFormat()))/8;
            mImageCallbackBuffer = new byte[size];
            mCamera.addCallbackBuffer(mImageCallbackBuffer);
            mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                mCamera.setPreviewTexture(mSurfaceTexture);
//            } else {
//                mCamera.setPreviewDisplay(null);
//            }
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public int findFrontFacingCamera(int init) {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(init == -1) {
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraId = i;
                    break;
                }
            }else{
                if (i != init) {
                    cameraId = i;
                    break;
                }
            }
        }
        return cameraId;
    }

    public void takePicture(final int cameraId, final Context context, final Handler handler, final PictureTakeListener run){
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                mCamera.startPreview();
                Bitmap bmp = BitmapUtils.decodeBitmapFromByteArray(bytes);
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId , info);
                Bitmap rotate = BitmapUtils.adjustPhotoRotation(bmp, info.orientation);
                Bitmap hover = BitmapUtils.horverImage(rotate,true,false);
                String imagepath = BitmapUtils.saveBmp2Gallery(context,hover,BitmapUtils.getPictureNameByDate());
                run.onPictureTake(hover, imagepath);
            }
        });
    }
    class CameraPreviewCallback implements Camera.PreviewCallback{
        @Override
        public void onPreviewFrame(byte[] var1, Camera var2){
            if (mCamera != null) {
                //mCamera.addCallbackBuffer(mImageCallbackBuffer);
            }
        }

    }
}
