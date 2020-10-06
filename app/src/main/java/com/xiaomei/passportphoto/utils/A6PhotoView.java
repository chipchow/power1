package com.xiaomei.passportphoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiaomei.passportphoto.R;

import org.opencv.core.Point;


public class A6PhotoView extends View {
    private Paint mPaint, mPaint2;
    private Context mContext;
    private Bitmap mSrcBitmap;
    private Bitmap mTmpBitmap;
    private Canvas mTmpCanvas;
    public int mWidth,mHeight;
    public int mPaperWidth,mPaperHeight,mSingleWidth,mSingleHeight,mCol,mRow,mGap,mLeftMargin,mTopMargin;
    public float mDisplayPPM;
    public float mRealPPM;//pix per milimeter
    public A6PhotoView(Context context) {
        super(context, null);
        init(context);
    }

    public A6PhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public A6PhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setClickable(true);
        mContext = context;

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.blue_blur));
        DashPathEffect dashPathEffect =
                new DashPathEffect(new float[]{50.0f, 20.0f}, 0);
        mPaint.setPathEffect(dashPathEffect);
        mPaperWidth = 152;
        mPaperHeight = 102;
        mDisplayPPM = 8;
        mRealPPM = 11.8f;
        mGap = 2;
        mTmpBitmap = Bitmap.createBitmap(mmToPx(mPaperWidth),mmToPx(mPaperHeight), Bitmap.Config.ARGB_8888);
        mTmpCanvas = new Canvas(mTmpBitmap);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mDisplayPPM = mWidth/mPaperHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Matrix matrix = new Matrix();
        matrix.setScale(mDisplayPPM/mRealPPM,mDisplayPPM/mRealPPM);
        matrix.postRotate(90);
        matrix.postTranslate(mWidth,0);
        canvas.drawBitmap(mTmpBitmap,matrix,mPaint);

        canvas.save();
    }

    public void setImageBitmap(Bitmap bitmap, int realW,int realH){
        mSrcBitmap = bitmap;
        mSingleWidth = realW;
        mSingleHeight = realH;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        mRealPPM = w*1.0f/realW;
        calculateMaxQuantity();
        mTmpCanvas.drawColor(Color.WHITE);
        for(int i=0;i<mRow;i++){
            for(int j=0;j<mCol;j++){
                mTmpCanvas.drawBitmap(mSrcBitmap,mmToPx(mLeftMargin)+j*(mmToPx(mGap)+w), mmToPx(mTopMargin)+i*(mmToPx(mGap)+h), mPaint);
            }
        }
    }

    public int mmToPx(int size){
        return (int)(size*mRealPPM);
    }
    private void calculateMaxQuantity() {
        mCol = Math.round(mPaperWidth / (mSingleWidth + mGap));
        mRow = Math.round(mPaperHeight / (mSingleHeight+ mGap));
        if(mRow > 2){
            mRow = 2;
        }
        if(mCol > 5){
            mCol = 5;
        }
        mLeftMargin = (mPaperWidth - mCol*mSingleWidth - (mCol-1)*mGap)/2;
        mTopMargin = (mPaperHeight - mRow*mSingleHeight - (mRow - 1)*mGap)/2;
    }
    public Bitmap getBitmap(){
        return mTmpBitmap;
    }
}
