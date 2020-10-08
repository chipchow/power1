package com.xiaomei.passportphoto.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.logic.PhotoAdaper;
import com.xiaomei.passportphoto.model.RunContext;

public class AccountOrderActivity extends BaseActivity {
    RecyclerView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_order);
        setHasTitle(true);
        setTitleText("我的订单");
        init();
    }

    public void init(){
        mListView = findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(layoutManager);
        PhotoAdaper adaper = new PhotoAdaper(RunContext.getInstance().mUser.getmPhotoList());
        mListView.setAdapter(adaper);
    }
}
