package com.xiaomei.passportphoto.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.activity.AccountAboutActivity;
import com.xiaomei.passportphoto.activity.AccountFeedbackActivity;
import com.xiaomei.passportphoto.activity.AccountOrderActivity;

/**
 * Created by Adib on 13-Apr-17.
 */

public class AccountFragment extends Fragment implements View.OnClickListener{

    Activity mActivity;
    View mRootView;
    View mLayout_order,mLayout_customer,mLayout_feedback,mLayout_score,mLayout_invite,mLayout_about,mLayout_update;
    Button mLogout;
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
        mLayout_order = mRootView.findViewById(R.id.layout_order);
        mLayout_customer = mRootView.findViewById(R.id.layout_customercenter);
        mLayout_feedback = mRootView.findViewById(R.id.layout_feedback);
        mLayout_score = mRootView.findViewById(R.id.layout_score);
        mLayout_invite = mRootView.findViewById(R.id.layout_invite);
        mLayout_about = mRootView.findViewById(R.id.layout_about);
        mLayout_update = mRootView.findViewById(R.id.layout_update);
        mLogout = mRootView.findViewById(R.id.button_logout);

        mLayout_order.setOnClickListener(this);
        mLayout_customer.setOnClickListener(this);
        mLayout_feedback.setOnClickListener(this);
        mLayout_score.setOnClickListener(this);
        mLayout_invite.setOnClickListener(this);
        mLayout_about.setOnClickListener(this);
        mLayout_update.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_order:{
                Intent intent = new Intent(mActivity, AccountOrderActivity.class);
                mActivity.startActivity(intent);
            }
                break;
            case R.id.layout_customercenter:
                break;
            case R.id.layout_feedback:{
                Intent intent = new Intent(mActivity, AccountFeedbackActivity.class);
                mActivity.startActivity(intent);
            }
                break;
            case R.id.layout_score:
                break;
            case R.id.layout_invite:
                break;
            case R.id.layout_about: {
                Intent intent = new Intent(mActivity, AccountAboutActivity.class);
                mActivity.startActivity(intent);
            }
                break;
            case R.id.layout_update:
                break;
            case R.id.button_logout:
                break;
            default:
                break;
        }
    }

}
