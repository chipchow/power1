package com.xiaomei.passportphoto.com.xiaomei.passportphoto.library;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaomei.passportphoto.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to handle bottom navigation UI and click events
 *
 * Created by Adib on 13-Apr-17.
 */

public class BottomNavigationBar implements View.OnClickListener {

    public static final int MENU_BAR_1 = 0;
    public static final int MENU_BAR_2 = 1;
    public static final int MENU_BAR_3 = 2;
    public static final int MENU_BAR_4 = 3;

    private List<NavigationPage> mNavigationPageList = new ArrayList<>();

    private Context mContext;
    private AppCompatActivity mActivity;
    private BottomNavigationMenuClickListener mListener;

    private LinearLayout mLLBar1, mLLBar2, mLLBar3, mLLBar4;
    private View mViewBar1, mViewBar2, mViewBar3, mViewBar4;
    private AppCompatImageView mImageViewBar1, mImageViewBar2, mImageViewBar3, mImageViewBar4;
    private AppCompatTextView mTextViewBar1, mTextViewBar2, mTextViewBar3, mTextViewBar4;
    private int mCurrent=0;

    public BottomNavigationBar(Context mContext, List<NavigationPage> pages, BottomNavigationMenuClickListener listener) {

        // initialize variables
        this.mContext = mContext;
        this.mActivity = (AppCompatActivity) mContext;
        this.mListener = listener;
        this.mNavigationPageList = pages;

        // getting reference to bar linear layout viewgroups
        this.mLLBar1 = (LinearLayout) mActivity.findViewById(R.id.linearLayoutBar1);
        this.mLLBar2 = (LinearLayout) mActivity.findViewById(R.id.linearLayoutBar2);
        this.mLLBar3 = (LinearLayout) mActivity.findViewById(R.id.linearLayoutBar3);
        this.mLLBar4 = (LinearLayout) mActivity.findViewById(R.id.linearLayoutBar4);

        // getting reference to bar upper highlight
        this.mViewBar1 = (View) mActivity.findViewById(R.id.viewBar1);
        this.mViewBar2 = (View) mActivity.findViewById(R.id.viewBar2);
        this.mViewBar3 = (View) mActivity.findViewById(R.id.viewBar3);
        this.mViewBar4 = (View) mActivity.findViewById(R.id.viewBar4);

        // getting reference to bar icons
        this.mImageViewBar1 = (AppCompatImageView) mActivity.findViewById(R.id.imageViewBar1);
        this.mImageViewBar2 = (AppCompatImageView) mActivity.findViewById(R.id.imageViewBar2);
        this.mImageViewBar3 = (AppCompatImageView) mActivity.findViewById(R.id.imageViewBar3);
        this.mImageViewBar4 = (AppCompatImageView) mActivity.findViewById(R.id.imageViewBar4);

        // setting the icons
        this.mImageViewBar1.setImageDrawable(mNavigationPageList.get(0).getIcon());
        this.mImageViewBar2.setImageDrawable(mNavigationPageList.get(1).getIcon());
        this.mImageViewBar3.setImageDrawable(mNavigationPageList.get(2).getIcon());
        this.mImageViewBar4.setImageDrawable(mNavigationPageList.get(3).getIcon());

        // getting reference to bar titles
        this.mTextViewBar1 = (AppCompatTextView) mActivity.findViewById(R.id.textViewBar1);
        this.mTextViewBar2 = (AppCompatTextView) mActivity.findViewById(R.id.textViewBar2);
        this.mTextViewBar3 = (AppCompatTextView) mActivity.findViewById(R.id.textViewBar3);
        this.mTextViewBar4 = (AppCompatTextView) mActivity.findViewById(R.id.textViewBar4);

        // 2etting the titles
        this.mTextViewBar1.setText(mNavigationPageList.get(0).getTitle());
        this.mTextViewBar2.setText(mNavigationPageList.get(1).getTitle());
        this.mTextViewBar3.setText(mNavigationPageList.get(2).getTitle());
        this.mTextViewBar4.setText(mNavigationPageList.get(3).getTitle());

        // setting click listeners
        this.mLLBar1.setOnClickListener(this);
        this.mLLBar2.setOnClickListener(this);
        this.mLLBar3.setOnClickListener(this);
        this.mLLBar4.setOnClickListener(this);
    }

    public int getCurrentTab(){
        return mCurrent;
    }

    public void setMainTab(int menuType){
        mCurrent = menuType;
        this.mViewBar1.setVisibility(View.INVISIBLE);
        this.mViewBar2.setVisibility(View.INVISIBLE);
        this.mViewBar3.setVisibility(View.INVISIBLE);
        this.mViewBar4.setVisibility(View.INVISIBLE);
        switch (menuType){
            case 0:
                mViewBar1.setVisibility(View.VISIBLE);
                this.mImageViewBar1.setImageDrawable(mNavigationPageList.get(0).getIcon_selected());
                this.mImageViewBar2.setImageDrawable(mNavigationPageList.get(1).getIcon());
                this.mImageViewBar3.setImageDrawable(mNavigationPageList.get(2).getIcon());
                this.mImageViewBar4.setImageDrawable(mNavigationPageList.get(3).getIcon());
                break;
            case 1:
                this.mViewBar2.setVisibility(View.VISIBLE);
                this.mImageViewBar1.setImageDrawable(mNavigationPageList.get(0).getIcon());
                this.mImageViewBar2.setImageDrawable(mNavigationPageList.get(1).getIcon_selected());
                this.mImageViewBar3.setImageDrawable(mNavigationPageList.get(2).getIcon());
                this.mImageViewBar4.setImageDrawable(mNavigationPageList.get(3).getIcon());
                break;
            case 2:
                break;
            case 3:
                this.mViewBar4.setVisibility(View.VISIBLE);
                this.mImageViewBar1.setImageDrawable(mNavigationPageList.get(0).getIcon());
                this.mImageViewBar2.setImageDrawable(mNavigationPageList.get(1).getIcon());
                this.mImageViewBar3.setImageDrawable(mNavigationPageList.get(2).getIcon());
                this.mImageViewBar4.setImageDrawable(mNavigationPageList.get(3).getIcon_selected());
                break;
            default:
                break;
        }

    }
    @Override
    public void onClick(View view) {
        // triggering click listeners
        if (view.getId() == R.id.linearLayoutBar1) {
            mListener.onClickedOnBottomNavigationMenu(MENU_BAR_1);
            return;
        } else if (view.getId() == R.id.linearLayoutBar2) {
            mListener.onClickedOnBottomNavigationMenu(MENU_BAR_2);
            return;
        } else if (view.getId() == R.id.linearLayoutBar3) {
            mListener.onClickedOnBottomNavigationMenu(MENU_BAR_3);
            return;
        } else if (view.getId() == R.id.linearLayoutBar4) {
            mListener.onClickedOnBottomNavigationMenu(MENU_BAR_4);
            return;
        } else {
            return;
        }

    }


    public interface BottomNavigationMenuClickListener {
        void onClickedOnBottomNavigationMenu(int menuType);
    }

}
