package com.xiaomei.passportphoto.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomei.passportphoto.R;

/**
 * Created by Adib on 13-Apr-17.
 */

public class AccountFragment extends Fragment {

    Activity mActivity;
    View mRootView;
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_account, container, false);
        return mRootView;
    }

}
