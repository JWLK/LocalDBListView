package com.jwlks.localdblistview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jwlks.localdblistview.Util.BaseDialog;
import com.jwlks.localdblistview.Util.showAlert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static Boolean checkUserSetting;
    LinearLayout userNotExist;
    LinearLayout userExist;
    LinearLayout userExistDataView;
    TextView textViewUserName;
    TextView textViewUserAge;
    TextView textViewUserDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        buttonEvent(getBaseContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserSetting = sharedPreferences.getBoolean("USER_ENABLE", false);
        if(!checkUserSetting) {
            Log.d("USER_ENABLE", "Value : " + checkUserSetting);
            userNotExist.setVisibility(View.VISIBLE);
            userExist.setVisibility(View.GONE);
            userExistDataView.setVisibility(View.GONE);

        } else {
            Log.d("USER_ENABLE", "Value : " + checkUserSetting);
            userNotExist.setVisibility(View.GONE);
            userExist.setVisibility(View.VISIBLE);
            userExistDataView.setVisibility(View.VISIBLE);

            textViewUserName.setText(sharedPreferences.getString("USER_NAME", ""));
            textViewUserAge.setText(sharedPreferences.getString("USER_AGE", ""));
            textViewUserDate.setText(sharedPreferences.getString("USER_DATE", ""));
        }

    }

    void buttonEvent(Context context) {
        TextView buttonUserManager = findViewById(R.id.user_manger);
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