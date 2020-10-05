package com.xiaomei.passportphoto.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.asynctask.SaveImageAsyncTask;
import com.xiaomei.passportphoto.logic.PhotoController;
import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;
import com.xiaomei.passportphoto.utils.BitmapUtils;
import com.xiaomei.passportphoto.utils.MyConstant;
import com.xiaomei.passportphoto.utils.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class ChangeBGActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandle = new Handler();

    private ProgressDialog dialog;

    private Button btn_single, btn_6inch,btn_save;
    private Button btn_red, btn_blue, btn_white,btn_confirm;
    private PhotoView imgPhoto;
    private ConstraintLayout mLayout_selectbg,mLayout_save,mLayout_image;

    int paperWidth = 98;
    int paperHeight = 152;
    int gap = 5;
    private int max, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changebg);
        init();
        control();
        String imagePath = getIntent().getStringExtra("filepath");
        imgPhoto.setImageBitmap(RunContext.getInstance().mBitmap,false, false);

        Photo p = new Photo();

        byte[] file = BitmapUtils.readFileToByteArray(imagePath);
        p.mPhotoOrigin = Util.uriEncode(Base64Util.encode(file),true);
        RunContext.getInstance().mUser.mCurrent = p;

        PhotoController.getInstance().sendMattingRequest(p, mHandle, new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                byte[] fg = RunContext.getInstance().mUser.mCurrent.mPhotoMat;
                BitmapUtils.saveByteArray(fg, BitmapUtils.filename2);
                Bitmap bitmap = BitmapUtils.decodeBitmapFromByteArray(fg);
                imgPhoto.setImageBitmap(bitmap,true,true);
                btn_white.setSelected(true);
                imgPhoto.setBackColor(ChangeBGActivity.this.getResources().getColor(R.color.photobackwhite));
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage("处理中，请稍候.");
        dialog.setCancelable(false);
        dialog.show();
    }


    private void init() {
        btn_save = findViewById(R.id.button_save);
        btn_6inch = findViewById(R.id.button_6inch);
        btn_single = findViewById(R.id.button_single);
        btn_red = findViewById(R.id.imageButton_red);
        btn_blue = findViewById(R.id.imageButton_blue);
        btn_white = findViewById(R.id.imageButton_white);
        btn_confirm = findViewById(R.id.imageView_confirm);

        mLayout_save = findViewById(R.id.constraint_save);
        mLayout_selectbg = findViewById(R.id.constraint_selectbg);
        btn_red.setSelected(false);
        btn_white.setSelected(true);
        btn_blue.setSelected(false);

        mLayout_image = findViewById(R.id.constraintLayout_image);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(this);
        lp.leftToLeft = ConstraintSet.PARENT_ID;
        lp.rightToRight = ConstraintSet.PARENT_ID;
        lp.topToTop = ConstraintSet.PARENT_ID;
        
        imgPhoto = new PhotoView(this);
        imgPhoto.setLayoutParams(lp);
    }

    private void control() {
        btn_save.setOnClickListener(this);
        btn_single.setOnClickListener(this);
        btn_6inch.setOnClickListener(this);

        btn_red.setOnClickListener(this);
        btn_blue.setOnClickListener(this);
        btn_white.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                Intent intent = new Intent(this,CropImageActivity.class);
                startActivity(intent);
                //saveBitmap();
                break;
            case R.id.button_6inch:
                //printBitmap();
                break;
            case R.id.button_single:
                break;
            case R.id.imageView_confirm:
                mLayout_selectbg.setVisibility(View.INVISIBLE);
                mLayout_save.setVisibility(View.VISIBLE);
                Bitmap bitmap = imgPhoto.getBitmap();
                imgPhoto.setImageBitmap(bitmap,false,false);
                break;
            case R.id.imageButton_red:
                btn_blue.setSelected(false);
                btn_white.setSelected(false);
                btn_red.setSelected(true);
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackred));
                break;
            case R.id.imageButton_blue:
                btn_blue.setSelected(true);
                btn_white.setSelected(false);
                btn_red.setSelected(false);
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackblue));
                break;
            case R.id.imageButton_white:
                btn_blue.setSelected(false);
                btn_white.setSelected(true);
                btn_red.setSelected(false);
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackwhite));
                break;
            default:break;
        }
    }

    private void saveBitmap(){
    }

    private void printBitmap(){
    }

    private int calculateMaxQuantity(Bitmap src, int paperWidth, int paperHeight, int gap) {
//        int x, y, max;
        int row, col;
        row = col = 0;
        paperWidth = MyConstant.mmToPx(paperWidth);
        paperHeight = MyConstant.mmToPx(paperHeight);
//        x = 0 + gap;
//        y = 0 + gap;
//        max = 0;
//        do {
//            max = max + 1;
//            x = x + src.getWidth() + gap;
//            if (x >= paperWidth - gap - src.getWidth()) {
//                x = 0 + gap;
//                y = y + src.getHeight() + gap;
//            }
//        }
//        while (x <= paperWidth - src.getWidth() - gap && y <= paperHeight - src.getHeight() - gap);
        col = Math.round(paperWidth / (src.getWidth() + gap));
        row = Math.round(paperHeight / (src.getHeight() + gap));
        return col * row;
    }

    private Bitmap drawImages(Bitmap src, int paperWidth, int paperHeight, int quantity, int max, int gap) {
        int x, y;
        paperWidth = MyConstant.mmToPx(paperWidth);
        paperHeight = MyConstant.mmToPx(paperHeight);
        x = 0 + gap;
        y = 0 + gap;
        Bitmap result = Bitmap.createBitmap(paperWidth, paperHeight, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < quantity; i++) {
            canvas.drawBitmap(src, x, y, null);
            x = x + src.getWidth() + gap;
            if (x > paperWidth - src.getWidth()) {
                x = 0 + gap;
                y = y + src.getHeight() + gap;
            }
        }
//        do {
//            canvas.drawBitmap(src, x, y, null);
//            x = x + src.getWidth() + gap;
//            if (x >= paperWidth - gap - src.getWidth()) {
//                x = 0 + gap;
//                y = y + src.getHeight() + gap;
//            }
//        }
//        while (x <= paperWidth - src.getWidth() - gap && y <= paperHeight - src.getHeight() - gap);

        return result;
    }




}
