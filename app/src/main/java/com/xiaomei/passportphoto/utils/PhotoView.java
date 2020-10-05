package com.xiaomei.passportphoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.asynctask.EyesRecognizeAsyncTask;
import com.xiaomei.passportphoto.model.RunContext;

import org.opencv.core.Point;

import java.util.concurrent.ExecutionException;

public class PhotoView extends View {

    private EyesRecognizeAsyncTask mEyesRecognizeAsyncTask;

    private Paint mPaint, mPaint2;
    private Context mContext;
    private Bitmap mBitmap;
    private int mBgColor;
    private float p1x, p1y, p2x, p2y;
    private Point point1, point2;
    public boolean mNeedEyeRecog = false;
    public int mWidth,mHeight;
    private float currentX, currentY, deltaX, deltaY, dx,dy;
    private boolean mEditable;//1,crop
    Rect mSrc = new Rect();
    Rect mDst = new Rect();
    public PhotoView(Context context) {
        super(context, null);
        init(context);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        super.setClickable(true);
        mContext = context;
        mNeedEyeRecog = false;
        mEditable = false;

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

        mPaint2 = new Paint();
        mPaint2.setStrokeWidth(15);
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(getResources().getColor(R.color.blur));

        p1x = p2x = p1y = p2y = 0.0f;
        deltaX = 0;
        deltaY = 0;
        mEyesRecognizeAsyncTask = new EyesRecognizeAsyncTask(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBgColor);
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        mSrc.left = (int)(-deltaX - dx);
        mSrc.top = (int)(-deltaY - dy);
        mSrc.right = (int)(-deltaX - dx + w);
        mSrc.bottom = (int)(-deltaY - dy + h);
        mDst.left = mDst.top = 0;
        mDst.right = mWidth;
        mDst.bottom = mHeight;
        canvas.drawBitmap(mBitmap,mSrc,mDst , mPaint);
//        canvas.drawBitmap(mBitmap,deltaX+dx,deltaY+dy,mPaint);
        if (mEditable){
            if (p1x != 0 && p1y != 0 && p2x != 0 && p2y != 0) {
                canvas.drawRect(p1x, p1y, p2x, p2y, mPaint);
            }
        }
        canvas.save();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBitmap != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    currentX = event.getX();
                    currentY = event.getY();
                    dx = 0;
                    dy = 0;
                    Log.e("touch","down:"+currentX+";"+currentY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    dx = event.getX() - currentX;
                    dy = event.getY() - currentY;
                    Log.e("touch","move current:"+event.getX()+";"+event.getY());
                    Log.e("touch","move delta:"+dx+";"+dy);
                    break;
                case MotionEvent.ACTION_UP:
                    currentX = 0;
                    currentY = 0;
                    deltaX += dx;
                    deltaY += dy;
                    dx = dy = 0;
                    Log.e("touch","up:"+deltaX+";"+deltaY);
                    break;
                default:break;
            }
        }
        invalidate();
        return true;
    }

    public void drawFaceRect(Point point1, Point point2) {
        float x1, y1, x2, y2;
        x1 = (float) point1.x;
        y1 = (float) point1.y;
        x2 = (float) point2.x;
        y2 = (float) point2.y;
        float whRatio = RunContext.getInstance().mSpec.mWidth*1.0f/RunContext.getInstance().mSpec.mHeight;
        float margin = (x2 - x1)*(2.2f*whRatio-0.5f);
        p1x = x1 - margin;
        p1y = y1 - (x2 - x1) *1.8f;
        p2x = x2 + margin;
        p2y = y1 + (x2 - x1) *2.6f;
        if(p1x < 0 || p2x > mWidth || p1y < 0 || p2y > mHeight){
            p1x = 0.1f*mWidth;
            p2x = 0.9f*mWidth;
            p1y = 0.1f*mHeight;
            p2y = 0.9f*mHeight;
        }
        invalidate();
    }

    public void setBackColor(int color){
        mBgColor = color;
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap,boolean needEyeRecog,boolean editable ) {
        mBitmap = bitmap;
        mBgColor = Color.WHITE;
        mNeedEyeRecog = needEyeRecog;
        mEditable = editable;
        if(mNeedEyeRecog) {
            mEyesRecognizeAsyncTask.execute(bitmap);
            try {
                this.point1 = mEyesRecognizeAsyncTask.get()[0];
                this.point2 = mEyesRecognizeAsyncTask.get()[1];
            } catch (Exception e) {
                e.printStackTrace();
                this.point1 = new Point(-1,-1);
                this.point2 = new Point(-1,-1);
            }
            drawFaceRect(this.point1, this.point2);
        }else{
            invalidate();
        }
    }

    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap((int)(p2x-p1x),(int)(p2y-p1y), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(mBgColor);
        Rect src = new Rect((int)(p1x-deltaX),(int)(p1y-deltaY),(int)(p2x-deltaX),(int)(p2y-deltaY));
        Rect dist = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawBitmap(mBitmap,src,dist,mPaint);
        return bitmap;
    }
}
