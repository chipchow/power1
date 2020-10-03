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

    private Button btn_save, btn_print;
    private ImageButton btn_red, btn_blue, btn_white;
    private ImageView imgPhoto;
    private Bitmap bitmapImage,currentBitmap;
    private int redBg = Color.RED;
    private int blueBg = Color.BLUE;
    private int whiteBg = Color.WHITE;

    int paperWidth = 98;
    int paperHeight = 152;
    int gap = 5;
    private int max, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_background);
        init();
        control();
        bitmapImage = PhotoSelectActivity.bitmapImage;
        imgPhoto.setImageBitmap(bitmapImage);

        Photo p = new Photo();

        BitmapUtils.saveBitmap(bitmapImage, BitmapUtils.filename);
        byte[] file = BitmapUtils.readFileToByteArray(BitmapUtils.filename);
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
                bitmapImage = BitmapUtils.decodeBitmapFromByteArray(fg);
                imgPhoto.setImageBitmap(bitmapImage);
                currentBitmap = bitmapImage;
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage("处理中，请稍候.");
        dialog.setCancelable(false);
        dialog.show();
//        MattingTask task = new MattingTask(this);
//        task.execute(bitmapImage);
    }


    private void init() {
        btn_save = findViewById(R.id.button_save);
        btn_print = findViewById(R.id.button_print);
        btn_red = findViewById(R.id.imageButton_red);
        btn_blue = findViewById(R.id.imageButton_blue);
        btn_white = findViewById(R.id.imageButton_white);
        imgPhoto = findViewById(R.id.imageView_photo);
    }

    private void control() {
        btn_save.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        btn_red.setOnClickListener(this);
        btn_blue.setOnClickListener(this);
        btn_white.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                saveBitmap();
                break;
            case R.id.button_print:
                printBitmap();
                break;
            case R.id.imageButton_red:
                currentBitmap = BitmapUtils.changeBackground(bitmapImage, redBg);
                imgPhoto.setImageBitmap(currentBitmap);
                break;
            case R.id.imageButton_blue:
                currentBitmap = BitmapUtils.changeBackground(bitmapImage, blueBg);
                imgPhoto.setImageBitmap(currentBitmap);
                break;
            case R.id.imageButton_white:
                currentBitmap = BitmapUtils.changeBackground(bitmapImage, whiteBg);
                imgPhoto.setImageBitmap(currentBitmap);
                break;
            default:break;
        }
    }

    private void saveBitmap(){
        File filepath = Environment.getExternalStorageDirectory();
        File myDir = new File(filepath.getAbsolutePath() + "/MyPhoto_Id/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = simpleDateFormat.format(new Date());
        String fname = "Image-" + date + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();

        SaveImageAsyncTask asyncTask = new SaveImageAsyncTask(this, file);
        asyncTask.execute(currentBitmap);
    }

    private void printBitmap(){
        quantity = max = calculateMaxQuantity(currentBitmap, paperWidth, paperHeight, gap);
        Bitmap bitmapOut = drawImages(currentBitmap, paperWidth, paperHeight, quantity, max, gap);
        imgPhoto.setImageBitmap(bitmapOut);

        File filepath = Environment.getExternalStorageDirectory();
        File myDir = new File(filepath.getAbsolutePath() + "/MyPhoto_Id/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = simpleDateFormat.format(new Date());
        String fname = "Image-" + date + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        SaveImageAsyncTask asyncTask = new SaveImageAsyncTask(this, file);
        asyncTask.execute(bitmapOut);
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


    public class MattingTask extends AsyncTask<Bitmap, Void, Void> {
        private ProgressDialog dialog;

        public MattingTask(Context activity) {
            dialog = new ProgressDialog(activity);
        }

        public void bodyseg(AipBodyAnalysis client, String filename) {
            // 传入可选参数调用接口
            HashMap<String, String> options = new HashMap<String, String>();
            options.put("type", "foreground");
            // 参数为二进制数组
            try {
                byte[] file = BitmapUtils.readFileToByteArray(filename);
                JSONObject res = client.bodySeg(file, options);
                String fg = (String) res.get("foreground");
                System.out.println(res.toString(2));
                byte[] bytes = android.util.Base64.decode(fg,android.util.Base64.DEFAULT);
                BitmapUtils.saveByteArray(bytes, BitmapUtils.filename2);
                bitmapImage = BitmapUtils.decodeBitmapFromByteArray(bytes);
                imgPhoto.setImageBitmap(bitmapImage);
                currentBitmap = bitmapImage;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("处理中，请稍候.");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            try {
                BitmapUtils.saveBitmap(bitmaps[0], BitmapUtils.filename);
//            bitmaps[0].recycle();
                AipBodyAnalysis client = BitmapUtils.getipBodyAnalysisInstance();
                bodyseg(client, BitmapUtils.filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

}
