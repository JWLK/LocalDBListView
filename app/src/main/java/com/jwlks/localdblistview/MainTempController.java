package com.jwlks.localdblistview;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jwlks.localdblistview.Temp.TempListViewAdapter;
import com.jwlks.localdblistview.Temp.TempListViewModel;
import com.jwlks.localdblistview.Temp.TempSqlOpenHelper;

public class MainTempController {

    /*DB Setting*/
    SQLiteDatabase tempDB;
    TempSqlOpenHelper helper = null;

    /*ListView Layout*/
    ListView tempListView;
    TempListViewModel tempModel;
    TempListViewAdapter tempAdapter;

    Boolean isTempExist = false;

    /*Item Layout*/
    TextView textViewUserName;
    TextView textViewUserAge;
    TextView textViewUserDate;


    private AppCompatActivity appCompatActivity;

    MainTempController(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    void initialize() {
        textViewUserName = appCompatActivity.findViewById(R.id.user_name);
        textViewUserAge = appCompatActivity.findViewById(R.id.user_age);
        textViewUserDate = appCompatActivity.findViewById(R.id.user_regist_date);
    }

}
