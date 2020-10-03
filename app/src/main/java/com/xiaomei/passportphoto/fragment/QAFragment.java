package com.xiaomei.passportphoto.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomei.passportphoto.R;

/**
 * Created by Adib on 13-Apr-17.
 */

public class QAFragment extends Fragment {

    public static QAFragment newInstance() {
        return new QAFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

}