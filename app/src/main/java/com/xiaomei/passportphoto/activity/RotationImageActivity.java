package com.xiaomei.passportphoto.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.asynctask.CheckAutoEnableAsyncTask;
import com.xiaomei.passportphoto.asynctask.MyAsyncTask;
import com.xiaomei.passportphoto.utils.MyConstant;
import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.min;

public class RotationImageActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "MainActivity";

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV Succesfully loaded!");
        } else {
            Log.d(TAG, "OpenCV not loaded");
        }
    }

    private Toolbar tbRotationImage;
    private ImageView imgPhoto;
    private ScrollingValuePicker svRuler;
    private ImageButton btnFlip, btnRotate;
    private Button btnAutoAdjustment;

    private static final int VALUE_MULTIPLE = 5;
    private static final float MIN_VALUE = -30.0f;
    private static final float MAX_VALUE = 30.0f;
    private static final float LINE_RULER_MULTIPLE_SIZE = 1.5f;
    private float angle = 0.0f;
    private int currentAngle = 0;
    private org.opencv.core.Point point1, point2;
    public static CascadeClassifier mCascadeClassifier;
//    private int orientation = 1;
    private CheckAutoEnableAsyncTask checkAutoEnableAsyncTask;

    public Bitmap imgBitmap, imgBitmapCpy;
    public static String filename = "";
    public static String filename2 = "";
    public static String filename0 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_image);
        init();
        controls();
    }

    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight)
    {
        if (bm == null)
        {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float scale = (scaleHeight>scaleWidth)?scaleWidth:scaleHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (bm != null & !bm.isRecycled())
        {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }

    private void init() {
        filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/tmp";
        filename2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/filename";
        filename0 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/0";
        tbRotationImage = findViewById(R.id.tb_RotationImage);
        imgPhoto = findViewById(R.id.img_RIPhoto);
        svRuler = findViewById(R.id.sv_Ruler);
        svRuler.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
        svRuler.setMaxValue(MIN_VALUE, MAX_VALUE);
        svRuler.setValueTypeMultiple(VALUE_MULTIPLE);
        svRuler.setInitValue(0);
        svRuler.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    svRuler.getScrollView().startScrollerTask();
                }
                return false;
            }
        });
        mCascadeClassifier = MyConstant.Cascade_Setting_Eye(this);
        btnFlip = findViewById(R.id.btn_RIFlip);
        btnRotate = findViewById(R.id.btn_RIRotate);
        btnAutoAdjustment = findViewById(R.id.btn_RIAutoAdjustment);
        setSupportActionBar(tbRotationImage);
        setTitle("旋转方向");
        loadingActionbar();
        Intent iReceive = getIntent();
        Uri imageUri = Uri.parse(iReceive.getStringExtra("img"));

        int desiredWidth = 320;
        int desiredHeight = 480;



        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            imgBitmap = BitmapFactory.decodeStream(inputStream);

            ExifInterface ei = new ExifInterface(inputStream);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    imgBitmap = rotateBitmap(imgBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    imgBitmap = rotateBitmap(imgBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    imgBitmap = rotateBitmap(imgBitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
            imgBitmap = rotateBitmap(imgBitmap, 270);
            imgBitmap = scaleImage(imgBitmap,desiredWidth,desiredHeight);
            imgBitmapCpy = imgBitmap.copy(imgBitmap.getConfig(), imgBitmap.isMutable());
            imgPhoto.setImageBitmap(imgBitmap);
            Log.d("uri", imageUri.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        checkAutoEnableAsyncTask = new CheckAutoEnableAsyncTask(this, btnAutoAdjustment);
        checkAutoEnableAsyncTask.execute(imgBitmap);
        try {
            point1 = checkAutoEnableAsyncTask.get()[0];
            point2 = checkAutoEnableAsyncTask.get()[1];
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadingActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tbRotationImage.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
    }

    private void controls() {
        tbRotationImage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        svRuler.setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableHorizontalScrollView observableHorizontalScrollView, int i, int i1) {
            }

            @Override
            public void onScrollStopped(int i, int i1) {
//                Log.d("RS", String.valueOf(DWUtils.getValueAndScrollItemToCenter(svRuler.getScrollView(),
//                        i, i1, MAX_VALUE, MIN_VALUE, svRuler.getViewMultipleSize())));
                int newAngle = DWUtils.getValueAndScrollItemToCenter(svRuler.getScrollView(),
                        i, i1, MAX_VALUE, MIN_VALUE, svRuler.getViewMultipleSize());
                float value = newAngle - currentAngle;
                currentAngle = newAngle;
                if (angle + value >= 360.0f) {
                    angle = angle + value - 360.0f;
                } else {
                    angle += value;
                }
//                imgBitmapCpy = rotateBitmap(imgBitmapCpy, (float) DWUtils.getValueAndScrollItemToCenter(svRuler.getScrollView(),
//                        i, i1, MAX_VALUE, MIN_VALUE, svRuler.getViewMultipleSize()));
                imgBitmapCpy = rotateBitmap(imgBitmap, angle);
                imgPhoto.setImageBitmap(imgBitmapCpy);
            }
        });

        btnRotate.setOnClickListener(this);
        btnFlip.setOnClickListener(this);
        btnAutoAdjustment.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mn_Check) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(imgBitmapCpy);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap flipBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_RIRotate:
                rotateImage();
                break;
            case R.id.btn_RIFlip:
                flipImage();
                break;
            case R.id.btn_RIAutoAdjustment:
                autoAdjustment(point1, point2);
                break;
        }
    }

    private void rotateImage() {
        angle = angle + 90.0f;
        if (angle >= 360.0f) {
            angle = angle - 360.0f;
        }
        imgBitmapCpy = rotateBitmap(imgBitmap, angle);
        imgPhoto.setImageBitmap(imgBitmapCpy);
    }

    private void flipImage() {
        imgBitmapCpy = flipBitmap(imgBitmapCpy);
        imgPhoto.setImageBitmap(imgBitmapCpy);
    }

    private double getAngle(org.opencv.core.Point firstPoint, org.opencv.core.Point secondPoint) {
        double dx, dy;
        dx = dy = 0;
        if (secondPoint.x >= firstPoint.x){
            dx = secondPoint.x - firstPoint.x;
            dy = secondPoint.y - firstPoint.y;
        }else {
            dx = firstPoint.x - secondPoint.x;
            dy = firstPoint.y - secondPoint.y;
        }

        double inRads = Math.atan2(dy, dx);
        return Math.toDegrees(inRads);
    }

    private void autoAdjustment(org.opencv.core.Point p1, org.opencv.core.Point p2) {

//        for (int i = 0; i < eyesArray.length; i++)
//            Imgproc.rectangle(myImageMat, eyesArray[i].tl(), eyesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
//        Utils.matToBitmap(myImageMat, imgBitmap);
//        imgPhoto.setImageBitmap(imgBitmap);
//        double angle1 = getAngle(eyesArray[0].tl(), eyesArray[1].tl());
        double angle1 = getAngle(p1, p2);
        Log.d("Angle", "" + String.valueOf(angle1));
        imgBitmapCpy = rotateBitmap(imgBitmap, (float) -angle1);
        imgPhoto.setImageBitmap(imgBitmapCpy);
    }


}
