package com.xiaomei.passportphoto.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.utils.BitmapUtils;

import org.opencv.android.CameraGLSurfaceView;

import android.hardware.Camera;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TakeActivity extends AppCompatActivity implements View.OnClickListener{
    CameraGLSurfaceView cameraGLSurfaceView;
    ImageView take_switchcamera;
    ImageView select_take;
    ImageView select_album;
    Handler mHandler = new Handler();
    Camera mCamera;
    Camera.Parameters mCameraParamters;
    Camera.PreviewCallback mCameraPreviewCallback;
    byte[] mImageCallbackBuffer;
    boolean mIsPreviewing;
    SurfaceHolder mSurfaceHolder;
    int mCameraId = -1;
    int m_W,m_H;
    static private SurfaceTexture mSurfaceTexture = new SurfaceTexture(11);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_take);
        init();
        control();
    }

    private void init(){
        take_switchcamera = findViewById(R.id.take_switchcamera);
        select_album = findViewById(R.id.select_album);
        select_take = findViewById(R.id.select_take);
        cameraGLSurfaceView = findViewById(R.id.cameraGLSurfaceView);
        mSurfaceHolder = cameraGLSurfaceView.getHolder();
        mCameraPreviewCallback = new CameraPreviewCallback();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                stopCamera();
            }
        });
    }

    private void control(){
        take_switchcamera.setOnClickListener(this);
        select_album.setOnClickListener(this);
        select_take.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_switchcamera:
                mCameraId = findFrontFacingCamera(mCameraId);
                stopCamera();
                startCamera(mCameraId);
                break;
            case R.id.select_album:
                break;
            case R.id.select_take:
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        mCamera.startPreview();
                        Bitmap bmp = BitmapUtils.decodeBitmapFromByteArray(bytes);
                        Bitmap rotate;
                        if(mCameraId == 0){
                            rotate = BitmapUtils.adjustPhotoRotation(bmp, 90);
                        }else {
                            rotate = BitmapUtils.adjustPhotoRotation(bmp, 270);
                        }
                        BitmapUtils.saveBmp2Gallery(TakeActivity.this,rotate,BitmapUtils.getPictureNameByDate());
                    }
                });
                break;
            default:
                break;
        }

    }
    private void stopCamera(){
        if (null == mCamera) {
            return;
        }
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mIsPreviewing = false;
    }

    private void startCamera(int cameraId){
        try {
            mCamera = Camera.open(mCameraId);
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
            mIsPreviewing = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void start(){
        if(mCameraId == -1) {
            mCameraId = findFrontFacingCamera(-1);
        }
        stopCamera();
        startCamera(mCameraId);
    }

    public static int findFrontFacingCamera(int init) {
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

    class CameraPreviewCallback implements Camera.PreviewCallback{
        @Override
        public void onPreviewFrame(byte[] var1, Camera var2){
            if (mCamera != null) {
                //mCamera.addCallbackBuffer(mImageCallbackBuffer);
            }
        }

    }

}
