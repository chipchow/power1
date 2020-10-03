package com.xiaomei.passportphoto.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.utils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNewImage, btnOldImage;
    private Uri imageUri;
    private static final int CAM_REQUEST = 1313;
    private static final int PICK_REQUEST = 1212;
    private static final int desiredWidth = 640;
    private static final int desiredHeight = 960;
    public static Bitmap bitmapImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photo_select);
        init();
        control();

    }


    private void init() {
        btnNewImage = findViewById(R.id.button_camera);
        btnOldImage = findViewById(R.id.button_album);

    }

    private void control() {
        btnNewImage.setOnClickListener(this);
        btnOldImage.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_camera:
                captureNewImage();
                break;
            case R.id.button_album:
                selectImageFromGallery();
                break;
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAM_REQUEST) {
            bitmapImage = (Bitmap) data.getExtras().get("data");
            bitmapImage = BitmapUtils.scaleImage(bitmapImage,desiredWidth,desiredHeight);

            imageUri = getImageUri(getApplicationContext(), bitmapImage);
            Intent iChangeBgItent = new Intent(PhotoSelectActivity.this, ChangeBGActivity.class);

            iChangeBgItent.putExtra("img", bitmapImage);
            startActivity(iChangeBgItent);
        } else if (resultCode == RESULT_OK && requestCode == PICK_REQUEST) {
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


                    bitmapImage = BitmapUtils.scaleImage(loadedImage,desiredWidth,desiredHeight);

                    Intent iChangeBgItent = new Intent(PhotoSelectActivity.this, ChangeBGActivity.class);
//                    iChangeBgItent.putExtra("img", bitmapImage);
                    startActivity(iChangeBgItent);
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
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void captureNewImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        File photoFile = null;
        photoFile = createImageFile();
        if(photoFile == null){
            return;
        }
        Uri photoURI = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(intent, CAM_REQUEST);

    }

        private File createImageFile() {
        try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir =
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            String imageFilePath = image.getAbsolutePath();
            return image;
        }catch (IOException e){
            e.printStackTrace();
            return null;
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
}
