package com.xiaomei.passportphoto.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xiaomei.passportphoto.R;
import com.xiaomei.passportphoto.activity.PhotoSelectActivity;
import com.xiaomei.passportphoto.activity.TakeActivity;
import com.xiaomei.passportphoto.model.PhotoSpec;

/**
 * Created by Adib on 13-Apr-17.
 */

public class PassportPhotoFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener listener;
    Activity mActivity;
    View mRootView;

    private ImageView button_1inch;
    private ImageView button_2inch;
    private ImageView button_less1inch;
    private ImageView button_less21inch;
    private ImageView button_more1inch;
    private ImageView button_custom;
    private EditText edit_width;
    private EditText edit_height;
    private EditText edit_smallest;
    private EditText edit_bigest;
    private EditText edit_dpi;
    private Button button_ok;


    private static final PhotoSpec s_1inch = new PhotoSpec(295,413,15,100,300);
    private static PhotoSpec s_2inch = new PhotoSpec(413,579,18,120,300);
    private static PhotoSpec s_less1inch = new PhotoSpec(260,378,10,80,300);
    private static PhotoSpec s_less2inch = new PhotoSpec(413,531,15,120,300);
    private static PhotoSpec s_more1inch = s_less2inch;

    public static PassportPhotoFragment newInstance() {
        return new PassportPhotoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_passport, container, false);
        init();
        control();
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(R.menu.option_menu_check,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        button_1inch = mRootView.findViewById(R.id.button_1inch);
        button_2inch = mRootView.findViewById(R.id.button_2inch);
        button_less1inch = mRootView.findViewById(R.id.button_less1inch);
        button_less21inch = mRootView.findViewById(R.id.button_less2inch);
        button_more1inch = mRootView.findViewById(R.id.button_more1inch);
        button_custom = mRootView.findViewById(R.id.button_custom);
//        edit_width = mRootView.findViewById(R.id.editText_width);
//        edit_height = mRootView.findViewById(R.id.editText_height);
//        edit_smallest = mRootView.findViewById(R.id.editText_smallest);
//        edit_bigest = mRootView.findViewById(R.id.editText_bigest);
//        edit_dpi = mRootView.findViewById(R.id.editText_dpi);
//        button_ok = mRootView.findViewById(R.id.button_OK);

//        button_ok.setEnabled(false);

//        button_ok.setOnClickListener(this);

        setEditText(false);
    }

    private void control() {
        button_1inch.setOnClickListener(this);
        button_2inch.setOnClickListener(this);
        button_less1inch.setOnClickListener(this);
        button_less21inch.setOnClickListener(this);
        button_more1inch.setOnClickListener(this);
        button_custom.setOnClickListener(this);
    }

    private void setEditText(boolean val){
//        edit_width.setEnabled(val);
//        edit_height.setEnabled(val);
//        edit_smallest.setEnabled(val);
//        edit_bigest.setEnabled(val);
//        edit_dpi.setEnabled(val);
    }

    private void fillEditText(PhotoSpec ps){
//        edit_width.setText(String.valueOf(ps.mWidth));
//        edit_height.setText(String.valueOf(ps.mHeight));
//        edit_smallest.setText(String.valueOf(ps.mSmallest));
//        edit_bigest.setText(String.valueOf(ps.mBigest));
//        edit_dpi.setText(String.valueOf(ps.mDPI));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1inch:
                fillEditText(s_1inch);
                Intent intent = new Intent(mActivity, TakeActivity.class);
                startActivity(intent);
//                button_ok.setEnabled(true);
                break;
            case R.id.button_2inch:
                fillEditText(s_2inch);
//                button_ok.setEnabled(true);
                break;
            case R.id.button_less1inch:
                fillEditText(s_less1inch);
//                button_ok.setEnabled(true);
                break;
            case R.id.button_less2inch:
                fillEditText(s_less2inch);
//                button_ok.setEnabled(true);
                break;
            case R.id.button_more1inch:
                fillEditText(s_more1inch);
//                button_ok.setEnabled(true);
                break;
            case R.id.button_custom:
                setEditText(true);
                break;
            case R.id.button_OK:
                Intent iPhotoSelectIntent = new Intent(mActivity, PhotoSelectActivity.class);
                startActivity(iPhotoSelectIntent);
                break;
            default:
                break;
        }
    }

}
