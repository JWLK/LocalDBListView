package com.jwlks.localdblistview.X;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jwlks.localdblistview.R;

public class Controller {

    /**
     * INIT VALUE
     */

    /*Color */
    int colorWhite;

    private AppCompatActivity appCompatActivity;

    Controller(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    void initialize() {
        getColorMaker();
        setXmlComponent();
        buttonEvent(appCompatActivity);
    }

    void getColorMaker() {
        //colorWhite = ContextCompat.getColor(appCompatActivity, R.color.white);
    }

    void setXmlComponent() {

    }

    void buttonEvent(Context context) {

    }



}


