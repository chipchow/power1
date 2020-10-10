package com.xiaomei.passportphoto.logic;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.utils.BitmapUtils;
import com.xiaomei.passportphoto.utils.UrlDrawable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PhotoAdaper extends RecyclerView.Adapter<PhotoAdaper.ViewHolder> {
    List<Photo> mPhotoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mSpec;
        TextView mSize;
        TextView mOrderNo;
        TextView mCost;
        Button mPay;
        Button mA6;
        Button mCancel;
        ConstraintLayout mPaidLayout,mUnpayLayout;
        public ViewHolder (View view){
            super(view);
            mImageView = view.findViewById(R.id.imageView_photo);
            mSpec = view.findViewById(R.id.textView_spec);
            mSize = view.findViewById(R.id.textView_size);
            mOrderNo = view.findViewById(R.id.textView_No);
            mCost = view.findViewById(R.id.textView_cost);
            mPay = view.findViewById(R.id.button_pay);
            mCancel = view.findViewById(R.id.button_cancel);
            mA6 = view.findViewById(R.id.button_A6);
            mPaidLayout = view.findViewById(R.id.layout_paid);
            mUnpayLayout = view.findViewById(R.id.layout_unpay);
        }
    }
    public PhotoAdaper(List<Photo> photoList){
        mPhotoList = photoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Photo p = mPhotoList.get(position);
        holder.mImageView.setImageDrawable(UrlDrawable.getDrawable(p.mThumbnailPath));
        holder.mSpec.setText(p.getSpecString());
        holder.mSize.setText(p.mSpec.mWidth + "x" + p.mSpec.mHeight + "px | " + p.mSpec.mSizeW + "x" + p.mSpec.mSizeH + "mm");
        holder.mOrderNo.setText(p.mOrderNo);

        if(p.mPaid) {
            holder.mPaidLayout.setVisibility(View.VISIBLE);
            holder.mUnpayLayout.setVisibility(View.INVISIBLE);
            holder.mCost.setText("¥" + p.mCost);
        }else{
            holder.mPaidLayout.setVisibility(View.INVISIBLE);
            holder.mUnpayLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount(){
        return mPhotoList.size();
    }



    }
