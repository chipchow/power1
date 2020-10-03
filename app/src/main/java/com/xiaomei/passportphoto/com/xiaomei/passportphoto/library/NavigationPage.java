package com.xiaomei.passportphoto.com.xiaomei.passportphoto.library;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public class NavigationPage {

    private String title;
    private Drawable icon;
    private Drawable icon_selected;
    private Fragment fragment;

    public NavigationPage(String title, Drawable icon, Drawable icon_selected, Fragment fragment) {
        this.title = title;
        this.icon = icon;
        this.icon_selected = icon_selected;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Drawable getIcon_selected(){
        return icon_selected;
    }

    public Fragment getFragment() {
        return fragment;
    }
}

