package com.xiaomei.passportphoto.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.utils.BitmapUtils;
import com.xiaomei.passportphoto.utils.CameraInterface;
import com.xiaomei.passportphoto.utils.PictureTakeListener;

import org.opencv.android.CameraGLSurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class TakeActivity extends BaseActivity implements View.OnClickListener{
    SurfaceView cameraGLSurfaceView;
    ImageView take_switchcamera;
    ImageView select_take;
    ImageView select_album;
    ConstraintLayout mLayout_take;
    ConstraintLayout mLayout_confirm;
    ImageView confirmImageView;
    SurfaceHolder mSurfaceHolder;
    Handler mHandler = new Handler();
    CameraInterface mCameraInterface;
    boolean mIsPreviewing = false;
    Button button_confirm;
    Button button_redo;
    int mCameraId = -1;
    Bitmap mOutputBitmap;
    private static final int PICK_REQUEST = 1212;
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
        mLayout_take = findViewById(R.id.layout_bottom_take);
        mLayout_confirm = findViewById(R.id.layout_bottom_confirm);
        button_confirm = findViewById(R.id.button_confirm);
        button_redo = findViewById(R.id.button_redo);
        confirmImageView = findViewById(R.id.confirmImageView);
        mLayout_confirm.setVisibility(View.INVISIBLE);
        mSurfaceHolder = cameraGLSurfaceView.getHolder();
        mCameraInterface = new CameraInterface(mSurfaceHolder);
        mIsPreviewing = true;
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(mIsPreviewing) {
                    if (mCameraId == -1) {
                        mCameraId = mCameraInterface.findFrontFacingCamera(-1);
                    }
                    mCameraInterface.startCamera(mCameraId);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mCameraInterface.stopCamera();
            }
        });
    }

    private void control(){
        take_switchcamera.setOnClickListener(this);
        select_album.setOnClickListener(this);
        select_take.setOnClickListener(this);
        button_redo.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_switchcamera:
                mCameraId = mCameraInterface.findFrontFacingCamera(mCameraId);
                mCameraInterface.stopCamera();
                mCameraInterface.startCamera(mCameraId);
                break;
            case R.id.select_album:
                mIsPreviewing = false;
                selectImageFromGallery();
                break;
            case R.id.select_take:
                mCameraInterface.takePicture(mCameraId,this, mHandler,new PictureTakeListener(){
                    @Override
                    public void onPictureTake(Bitmap bmp, String imagePath) {
                        mLayout_take.setVisibility(View.INVISIBLE);
                        mLayout_confirm.setVisibility(View.VISIBLE);
                        cameraGLSurfaceView.setVisibility(View.INVISIBLE);
                        confirmImageView.setVisibility(View.VISIBLE);
                        confirmImageView.setImageBitmap(bmp);
                        mOutputBitmap = bmp;
                        BitmapUtils.saveBitmap(bmp,BitmapUtils.getTmpPath(TakeActivity.this));
                    }
                });
                break;
            case R.id.button_confirm:
                Intent iChangeBgItent = new Intent(TakeActivity.this, ChangeBGActivity.class);
                iChangeBgItent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                iChangeBgItent.putExtra("filepath",BitmapUtils.getTmpPath(TakeActivity.this));
                RunContext.getInstance().mBitmap = mOutputBitmap;
                startActivity(iChangeBgItent);
                finish();
                break;
            case R.id.button_redo:
                mLayout_confirm.setVisibility(View.INVISIBLE);
                mLayout_take.setVisibility(View.VISIBLE);
                cameraGLSurfaceView.setVisibility(View.VISIBLE);
                confirmImageView.setVisibility(View.INVISIBLE);
                mCameraInterface.stopCamera();
                mCameraInterface.startCamera(mCameraId);
                break;
            default:
                break;
        }

    }

    private void selectImageFromGallery() {
        Intent iToGallery = new Intent(Intent.ACTION_PICK);
        File fileInput = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String stringFile = fileInput.getPath();
        Uri data = Uri.parse(stringFile);
        iToGallery.setDataAndType(data, "image/*");
        startActivityForResult(iToGallery, PICK_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (resultCode == RESULT_OK && requestCode == PICK_REQUEST) {
            imageUri = data.getData();
            loadImage(imageUri);
        }
    }

    public void loadImage(Uri imageUri){

        try {
            SimpleTarget target = new SimpleTarget<Bitmap>() {

                @SuppressLint("NewApi")
                @Override
                public void onResourceReady(Bitmap loadedImage,
                                            GlideAnimation<? super Bitmap> arg1) {
                    Bitmap bitmapImage = BitmapUtils.scaleImage(loadedImage,960,1280);
                    BitmapUtils.saveBitmap(bitmapImage,BitmapUtils.getTmpPath(TakeActivity.this));
                    Intent iChangeBgItent = new Intent(TakeActivity.this, ChangeBGActivity.class);
                    iChangeBgItent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    iChangeBgItent.putExtra("filepath",BitmapUtils.getTmpPath(TakeActivity.this));
                    RunContext.getInstance().mBitmap = bitmapImage;
                    startActivity(iChangeBgItent);
                    finish();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    // TODO Auto-generated method stub
                    super.onLoadFailed(e, errorDrawable);
                }

            };
            Glide.with(this).load(imageUri).asBitmap().into(target);
            Log.d("uri", imageUri.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
