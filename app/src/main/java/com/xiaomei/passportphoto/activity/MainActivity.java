package com.xiaomei.passportphoto.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.com.xiaomei.passportphoto.library.BottomBarHolderActivity;
import com.xiaomei.passportphoto.com.xiaomei.passportphoto.library.NavigationPage;
import com.xiaomei.passportphoto.fragment.AccountFragment;
import com.xiaomei.passportphoto.fragment.PassportPhotoFragment;
import com.xiaomei.passportphoto.fragment.PhotoStudioFragment;
import com.xiaomei.passportphoto.fragment.QAFragment;
import com.xiaomei.passportphoto.logic.PhotoController;
import com.xiaomei.passportphoto.model.RunContext;
import com.xiaomei.passportphoto.model.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BottomBarHolderActivity implements PassportPhotoFragment.OnFragmentInteractionListener, PhotoStudioFragment.OnFragmentInteractionListener {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public Fragment[] mCurrentFragment;
    public Handler mHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.e("权限检测", "读或写文件权限未获取");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        mCurrentFragment = new Fragment[4];
        mCurrentFragment[0] = PassportPhotoFragment.newInstance();
        mCurrentFragment[1] = PhotoStudioFragment.newInstance();
        mCurrentFragment[2] = QAFragment.newInstance();
        mCurrentFragment[3]= AccountFragment.newInstance();
        NavigationPage page1 = new NavigationPage("证件照", ContextCompat.getDrawable(this, R.drawable.passport_unsel),ContextCompat.getDrawable(this,R.drawable.passport_select), mCurrentFragment[0]);
        NavigationPage page2 = new NavigationPage("规格", ContextCompat.getDrawable(this, R.drawable.shop_unsel), ContextCompat.getDrawable(this,R.drawable.shop_selected), mCurrentFragment[1]);
        NavigationPage page3 = new NavigationPage("客服区", ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), mCurrentFragment[2]);
        NavigationPage page4 = new NavigationPage("我的", ContextCompat.getDrawable(this, R.drawable.account_unsel), ContextCompat.getDrawable(this, R.drawable.account_selected), mCurrentFragment[3]);

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);

        initUser();
    }

    public void initUser(){
        RunContext.getInstance().mUser = new User();
        RunContext.getInstance().mUser.mUserID = "123456789";

        if(!PhotoController.getInstance().sessionIsValid()){
            PhotoController.getInstance().sendLoginRequest(mHandle, new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("permission", "onRequestPermissionsResult  requestCode" + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "run writeDatasToExternalStorage() method", Toast.LENGTH_SHORT).show();
                    //   writeDatasToExternalStorage();

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }

}