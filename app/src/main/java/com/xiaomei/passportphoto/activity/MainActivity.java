package com.xiaomei.passportphoto.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xiaomei.passportphoto.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnNewImage, btnOldImage;
    private Uri imageUri;
    private static final int CAM_REQUEST = 1313;
    private static final int PICK_REQUEST = 1212;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Bitmap bitmapImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        control();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("permission", "onRequestPermissionsResult  requestCode" + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(MainActivity.this, "run writeDatasToExternalStorage() method", Toast.LENGTH_SHORT).show();
                    //   writeDatasToExternalStorage();

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
        private void init() {
        btnNewImage = findViewById(R.id.btn_NewImage);
        btnOldImage = findViewById(R.id.btn_OldImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("权限检测", "读或写文件权限未获取");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void control() {
        btnNewImage.setOnClickListener(this);
        btnOldImage.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_NewImage:
                captureNewImage();
                break;
            case R.id.btn_OldImage:
                selectImageFromGallery();
                break;
        }
    }


    private void captureNewImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM_REQUEST);

//        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (photoFile != null){
//                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.provider",photoFile);
//
//            }
//            startActivityForResult(pictureIntent, CAM_REQUEST);
//        }

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
        Intent iToRotationImage = new Intent(MainActivity.this, RotationImageActivity.class);
        if (resultCode == RESULT_OK && requestCode == CAM_REQUEST) {
//            bitmapImage = (Bitmap) data.getExtras().get("data");
//
//            imageUri = getImageUri(getApplicationContext(), bitmapImage);
//            iToRotationImage.putExtra("img", imageUri.toString());
//            startActivity(iToRotationImage);


        } else if (resultCode == RESULT_OK && requestCode == PICK_REQUEST) {
            imageUri = data.getData();
            iToRotationImage.putExtra("img", imageUri.toString());
            startActivity(iToRotationImage);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


//    private File createImageFile() throws IOException {
//        String timeStamp =
//                new SimpleDateFormat("yyyyMMdd_HHmmss",
//                        Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + "_";
//        File storageDir =
//                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        imageFilePath = image.getAbsolutePath();
//        return image;
//    }
}
