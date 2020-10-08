package com.xiaomei.passportphoto.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.logic.PhotoController;
import com.xiaomei.passportphoto.model.Photo;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.utils.A6PhotoView;
import com.xiaomei.passportphoto.utils.BitmapUtils;
import com.xiaomei.passportphoto.utils.PhotoView;

public class ChangeBGActivity extends BaseActivity implements View.OnClickListener {
    private Handler mHandle = new Handler();

    private ProgressDialog dialog;

    private RadioButton btn_single, btn_6inch;
    private Button btn_save;
    private Button btn_red, btn_blue, btn_white,btn_confirm;
    private PhotoView imgPhoto;
    private A6PhotoView mA6PhotoView;
    private ConstraintLayout mLayout_selectbg,mLayout_save,mLayout_image;
    private Bitmap mFinalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_changebg);
        setHasTitle(true);
        setTitleText("编辑");
        init();
        control();
        String imagePath = getIntent().getStringExtra("filepath");
        imgPhoto.setImageBitmap(RunContext.getInstance().mBitmap,false, false);

        Photo p = new Photo();

        byte[] file = BitmapUtils.readFileToByteArray(imagePath);
        p.mNetForMatting_PhotoOrigin = Util.uriEncode(Base64Util.encode(file),true);
        RunContext.getInstance().mUser.mCurrent = p;

        PhotoController.getInstance().sendMattingRequest(p, mHandle, new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                byte[] fg = RunContext.getInstance().mUser.mCurrent.mNetForMatting_PhotoMat;
                Bitmap bitmap = BitmapUtils.decodeBitmapFromByteArray(fg);
                imgPhoto.setImageBitmap(bitmap,true,true);
                btn_white.setSelected(true);
                imgPhoto.setBackColor(ChangeBGActivity.this.getResources().getColor(R.color.photobackwhite));
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage("处理中，请稍候.");
        dialog.setCancelable(true);
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
        int w = RunContext.getInstance().mBitmap.getWidth();
        int h = RunContext.getInstance().mBitmap.getHeight();
        addPhotoViewToLayout(w,h);
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
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_save:
                addPhotoToList();
//                Intent intent = new Intent(this,CropImageActivity.class);
//                startActivity(intent);
                uploadPhoto();
                break;
            case R.id.button_6inch:
                btn_single.setChecked(false);
                btn_6inch.setChecked(true);
                showA6Photo();
                break;
            case R.id.button_single:
                btn_single.setChecked(true);
                btn_6inch.setChecked(false);
                showSinglePhoto();
                break;
            case R.id.imageView_confirm:
                mLayout_selectbg.setVisibility(View.INVISIBLE);
                mLayout_save.setVisibility(View.VISIBLE);
                mFinalBitmap = imgPhoto.getBitmap();
                addPhotoViewToLayout(mFinalBitmap.getWidth(),mFinalBitmap.getHeight());
                imgPhoto.setImageBitmap(mFinalBitmap,false,false);
                btn_single.setChecked(true);
                btn_6inch.setChecked(false);
                break;
            case R.id.imageButton_red:
                btn_blue.setSelected(false);
                btn_white.setSelected(false);
                btn_red.setSelected(true);
                RunContext.getInstance().mBackground = 0;
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackred));
                break;
            case R.id.imageButton_blue:
                btn_blue.setSelected(true);
                btn_white.setSelected(false);
                btn_red.setSelected(false);
                RunContext.getInstance().mBackground = 2;
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackblue));
                break;
            case R.id.imageButton_white:
                btn_blue.setSelected(false);
                btn_white.setSelected(true);
                btn_red.setSelected(false);
                RunContext.getInstance().mBackground = 1;
                imgPhoto.setBackColor(this.getResources().getColor(R.color.photobackwhite));
                break;
            default:break;
        }
    }

    private void addPhotoViewToLayout(int w, int h){
        mLayout_image.removeAllViews();
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w,h);
        lp.leftToLeft = ConstraintSet.PARENT_ID;
        lp.rightToRight = ConstraintSet.PARENT_ID;
        lp.topToTop = ConstraintSet.PARENT_ID;
        lp.bottomToBottom = ConstraintSet.PARENT_ID;
        imgPhoto = new PhotoView(this);
        imgPhoto.setLayoutParams(lp);
        mLayout_image.addView(imgPhoto,lp);
    }

    private void showSinglePhoto(){
        addPhotoViewToLayout(mFinalBitmap.getWidth(),mFinalBitmap.getHeight());
        imgPhoto.setImageBitmap(mFinalBitmap,false,false);
    }

    private void showA6Photo(){
        int w = 102*8;
        int h = 152*8;
        mLayout_image.removeAllViews();
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w,h);
        lp.leftToLeft = ConstraintSet.PARENT_ID;
        lp.rightToRight = ConstraintSet.PARENT_ID;
        lp.topToTop = ConstraintSet.PARENT_ID;
        lp.bottomToBottom = ConstraintSet.PARENT_ID;
        mA6PhotoView = new A6PhotoView(this);
        mA6PhotoView.setLayoutParams(lp);
        mLayout_image.addView(mA6PhotoView,lp);
        mA6PhotoView.setImageBitmap(mFinalBitmap,RunContext.getInstance().mSpec.mSizeW,RunContext.getInstance().mSpec.mSizeH);
    }

    private String addPhotoToList(){
        String pid = RunContext.getInstance().mUser.mCurrent.mPID;
        Photo p = new Photo(pid,RunContext.getInstance().mSpecType);
        p.mSpec = RunContext.getInstance().mSpec;
        String imagePath = BitmapUtils.getThumbPath(this, pid);
        Bitmap thumb = BitmapUtils.getThumbnailPhoto(mFinalBitmap);
        BitmapUtils.saveBitmap(thumb,imagePath);
        p.mThumbnailPath = imagePath;
        p.mBackGround = RunContext.getInstance().mBackground;
        RunContext.getInstance().mUser.getmPhotoList().add(p);
        return imagePath;
    }

    private void uploadPhoto(){
        Photo p = RunContext.getInstance().mUser.mCurrent;
        String imagePath = BitmapUtils.getTmpPath(this);

        BitmapUtils.saveBitmap(mFinalBitmap,imagePath);
        byte[] file = BitmapUtils.readFileToByteArray(imagePath);
        p.mNetForUpload_PhotoPost = Util.uriEncode(Base64Util.encode(file),true);

        file = BitmapUtils.readFileToByteArray(p.mThumbnailPath);
        p.mNetForUpload_PhotoThumbnail = Util.uriEncode(Base64Util.encode(file),true);

        PhotoController.getInstance().sendUploadRequest(p, mHandle, new Runnable() {
            public void run(){
                if(btn_6inch.isChecked()){
                    BitmapUtils.saveBmp2Gallery(ChangeBGActivity.this,mA6PhotoView.getBitmap(),BitmapUtils.getPictureNameByDate()+"_A6");
                }else{
                    BitmapUtils.saveBmp2Gallery(ChangeBGActivity.this,mFinalBitmap,BitmapUtils.getPictureNameByDate()+"_single");
                }
                Toast.makeText(ChangeBGActivity.this,"保存成功",Toast.LENGTH_LONG).show();
            }

        });
    }






}
