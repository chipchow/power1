package com.xiaomei.passportphoto.activity;


import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomei.passportphoto.R;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    public ConstraintLayout mTitleBar;
    private boolean mHasTitle;
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
    public void setHasTitle(boolean has){
        if(has) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.layout_titlebar);
            mTitleBar = findViewById(R.id.title_bar);
            mLeft = findViewById(R.id.imageLeft);
            mRight = findViewById(R.id.imageRight);
            mTitle = findViewById(R.id.middleText);
            mHasTitle = true;
            mLeft.setOnClickListener(this);
            mRight.setOnClickListener(this);
        }
    }

    public View getmTitleBar(){
        return mTitleBar;
    }

    public void setTitleText(String title){
        mTitle.setText(title);
    }

    public void setRightShow(boolean show){
        if(show && mTitleBar != null) {
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
                    finish();
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
