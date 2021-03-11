package com.jwlks.localdblistview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jwlks.localdblistview.Account.UserPanel;
import com.jwlks.localdblistview.Temp.TempListViewAdapter;
import com.jwlks.localdblistview.Temp.TempListViewModel;
import com.jwlks.localdblistview.Temp.TempSqlOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    AppCompatActivity appCompatActivity;
    Context context;

    /*sharePreferences*/
    public static SharedPreferences sharedPreferences;
    public static Boolean checkUserSetting;
    public static int checkUserIdNumber;
    int saveUserId = 0;

    /*xmlComponent*/
    LinearLayout userNotExist;
    LinearLayout userExist;
    LinearLayout userExistDataView;
    TextView textViewUserName;
    TextView textViewUserAge;
    TextView textViewUserDate;

    /*Controller*/
    public MainTempController mainTempController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appCompatActivity = this;
        context = this;

        /*sharePreferences*/
        sharedPreferences = getSharedPreferences("initReference", MODE_PRIVATE);
        checkUserSetting = sharedPreferences.getBoolean("USER_ENABLE", false);

        Log.d("USER SETTING", "SETTING : " + checkUserSetting);

        /*User Panel*/
        userNotExist = findViewById(R.id.user_not_exist);
        userExist = findViewById(R.id.user_exist);
        userExistDataView = findViewById(R.id.user_exist_data_view);
        textViewUserName = findViewById(R.id.user_name);
        textViewUserAge = findViewById(R.id.user_age);
        textViewUserDate = findViewById(R.id.user_regist_date);

        buttonEvent();

        /*Temp View Controller*/
        mainTempController = new MainTempController(this);
        mainTempController.initialize();

        /*Graph View Controller*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserSetting = sharedPreferences.getBoolean("USER_ENABLE", false);
        Log.d("USER_ENABLE", "Value : " + checkUserSetting);
        if(!checkUserSetting) {
            userNotExist.setVisibility(View.VISIBLE);
            userExist.setVisibility(View.GONE);
            userExistDataView.setVisibility(View.GONE);

        } else {
            userNotExist.setVisibility(View.GONE);
            userExist.setVisibility(View.VISIBLE);
            userExistDataView.setVisibility(View.VISIBLE);

            textViewUserName.setText(sharedPreferences.getString("USER_NAME", ""));
            textViewUserAge.setText(sharedPreferences.getString("USER_AGE", ""));
            textViewUserDate.setText(sharedPreferences.getString("USER_DATE", ""));
        }

        checkUserIdNumber = sharedPreferences.getInt("USER_ID", 0);
        if(checkUserIdNumber != saveUserId){
            Log.d("USER_ID", "Value : " + checkUserIdNumber);
            saveUserId = checkUserIdNumber;
            MainTempController.tempAdapter = MainTempController.setTempAdapter(appCompatActivity, MainTempController.tempDB);
            MainTempController.tempListView = MainTempController.setListViewTemp("temp", MainTempController.tempAdapter, appCompatActivity);
            MainTempController.tempAdapter.sortIdDesc();
        } else {
            Log.d("USER_ID", "Value : " + "NO CHANGE");
        }



    }

    void buttonEvent() {
        LinearLayout buttonUserManager = findViewById(R.id.button_user_manager);
        buttonUserManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), UserPanel.class)); //로딩이 끝난 후, MainActivity 이동
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("USER_MODE", false);
                editor.apply();
            }
        });

    }


}