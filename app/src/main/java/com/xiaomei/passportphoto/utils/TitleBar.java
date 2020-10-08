package com.xiaomei.passportphoto.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomei.passportphoto.R;

public class TitleBar extends ConstraintLayout implements View.OnClickListener{
    public interface OnLeftClickListener{
        public void onClick();
    }
    public interface OnRightClickListener{
        public void onClick();
    }
    public ImageView mLeft,mRight;
    TextView mTitle;
    OnLeftClickListener mLeftListener;
    OnRightClickListener mRightListener;

    public TitleBar(Context context) {
        super(context, null);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_titlebar,this);
        mLeft = findViewById(R.id.imageLeft);
        mRight = findViewById(R.id.imageRight);
        mTitle = findViewById(R.id.middleText);
    }

    public void setBgTransparent(){
        setAlpha(1);
    }

    public void setTitleText(String title){
        mTitle.setText(title);
    }

    public void setRightShow(boolean show){
        if(show) {
            mRight.setVisibility(View.VISIBLE);
        }else{
            mRight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imageLeft:
                if(mLeftListener != null){
                    mLeftListener.onClick();
                }else{
                    
                }
                break;
            case R.id.imageRight:
                if(mRightListener != null){
                    mRightListener.onClick();
                }
                break;
            default:
                break;
        }
    }
}
