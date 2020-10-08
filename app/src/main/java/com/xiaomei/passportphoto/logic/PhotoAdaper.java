package com.xiaomei.passportphoto.logic;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.utils.BitmapUtils;

import java.io.File;
import java.util.List;

public class PhotoAdaper extends RecyclerView.Adapter<PhotoAdaper.ViewHolder> {
    List<Photo> mPhotoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mSpec;
        TextView mSize;
        TextView mOrderNo;
        TextView mCost;
        public ViewHolder (View view){
            super(view);
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
        holder.mImageView.setImageURI(Uri.fromFile(new File(p.mThumbnailPath)));
        holder.mSpec.setText(p.getSpecString());
        holder.mSize.setText(p.mSpec.mWidth+"x"+p.mSpec.mHeight+"px | "+p.mSpec.mSizeW+"x"+p.mSpec.mSizeH+"mm");
        holder.mOrderNo.setText(p.mOrderNo);
        holder.mCost.setText("¥"+p.mCost);
    }

    @Override
    public int getItemCount(){
        return mPhotoList.size();
    }
}
