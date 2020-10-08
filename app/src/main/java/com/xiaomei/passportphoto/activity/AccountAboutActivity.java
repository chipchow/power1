package com.xiaomei.passportphoto.activity;

import android.os.Bundle;
import android.view.Window;

import com.xiaomei.passportphoto.R;

public class AccountAboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_about);
        setHasTitle(true);
        setTitleText("关于");
    }
}
