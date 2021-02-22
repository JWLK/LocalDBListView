package com.jwlks.localdblistview.X;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jwlks.localdblistview.R;


public class Blank extends AppCompatActivity {

    //Controller Value init
    public Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_blank);

        //Controller Value Setting
        controller = new Controller(this);


    }
}
