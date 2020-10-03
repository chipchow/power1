package com.xiaomei.passportphoto.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.model.PhotoSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SizeSelectActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Button button_1inch;
    private Button button_2inch;
    private Button button_less1inch;
    private Button button_less21inch;
    private Button button_more1inch;
    private Button button_custom;
    private EditText edit_width;
    private EditText edit_height;
    private EditText edit_smallest;
    private EditText edit_bigest;
    private EditText edit_dpi;
    private Button button_ok;


    private static final PhotoSpec s_1inch = new PhotoSpec(295,413,15,100,300);
    private static PhotoSpec s_2inch = new PhotoSpec(413,579,18,120,300);
    private static PhotoSpec s_less1inch = new PhotoSpec(260,378,10,80,300);
    private static PhotoSpec s_less2inch = new PhotoSpec(413,531,15,120,300);
    private static PhotoSpec s_more1inch = s_less2inch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_size_select);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("权限检测", "读或写文件权限未获取");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
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
                    Toast.makeText(SizeSelectActivity.this, "run writeDatasToExternalStorage() method", Toast.LENGTH_SHORT).show();
                    //   writeDatasToExternalStorage();

                } else {
                    // Permission Denied
                    Toast.makeText(SizeSelectActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
        button_1inch = findViewById(R.id.button_1inch);
        button_2inch = findViewById(R.id.button_2inch);
        button_less1inch = findViewById(R.id.button_less1inch);
        button_less21inch = findViewById(R.id.button_less2inch);
        button_more1inch = findViewById(R.id.button_more1inch);
        button_custom = findViewById(R.id.button_custom);
        edit_width = findViewById(R.id.editText_width);
        edit_height = findViewById(R.id.editText_height);
        edit_smallest = findViewById(R.id.editText_smallest);
        edit_bigest = findViewById(R.id.editText_bigest);
        edit_dpi = findViewById(R.id.editText_dpi);
        button_ok = findViewById(R.id.button_OK);

        button_ok.setEnabled(false);

        button_1inch.setOnClickListener(this);
        button_2inch.setOnClickListener(this);
        button_less1inch.setOnClickListener(this);
        button_less21inch.setOnClickListener(this);
        button_more1inch.setOnClickListener(this);
        button_custom.setOnClickListener(this);
        button_ok.setOnClickListener(this);

        setEditText(false);
    }

    private void control() {
        button_1inch.setOnClickListener(this);
        button_2inch.setOnClickListener(this);
        button_less1inch.setOnClickListener(this);
        button_less21inch.setOnClickListener(this);
        button_more1inch.setOnClickListener(this);
        button_custom.setOnClickListener(this);
    }

    private void setEditText(boolean val){
        edit_width.setEnabled(val);
        edit_height.setEnabled(val);
        edit_smallest.setEnabled(val);
        edit_bigest.setEnabled(val);
        edit_dpi.setEnabled(val);
    }

    private void fillEditText(PhotoSpec ps){
        edit_width.setText(String.valueOf(ps.mWidth));
        edit_height.setText(String.valueOf(ps.mHeight));
        edit_smallest.setText(String.valueOf(ps.mSmallest));
        edit_bigest.setText(String.valueOf(ps.mBigest));
        edit_dpi.setText(String.valueOf(ps.mDPI));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1inch:
                fillEditText(s_1inch);
                button_ok.setEnabled(true);
                break;
            case R.id.button_2inch:
                fillEditText(s_2inch);
                button_ok.setEnabled(true);
                break;
            case R.id.button_less1inch:
                fillEditText(s_less1inch);
                button_ok.setEnabled(true);
                break;
            case R.id.button_less2inch:
                fillEditText(s_less2inch);
                button_ok.setEnabled(true);
                break;
            case R.id.button_more1inch:
                fillEditText(s_more1inch);
                button_ok.setEnabled(true);
                break;
            case R.id.button_custom:
                setEditText(true);
                break;
            case R.id.button_OK:
                Intent iPhotoSelectIntent = new Intent(SizeSelectActivity.this, PhotoSelectActivity.class);
                startActivity(iPhotoSelectIntent);
                break;
            default:
                break;
        }
    }



}
