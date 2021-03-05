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

    Context context;

    /*sharePreferences*/
    public static SharedPreferences sharedPreferences;
    public static Boolean checkUserSetting;
    LinearLayout userNotExist;
    LinearLayout userExist;
    LinearLayout userExistDataView;
    TextView textViewUserName;
    TextView textViewUserAge;
    TextView textViewUserDate;

    /*DB Setting*/
    SQLiteDatabase tempDB;
    TempSqlOpenHelper helper = null;

    /*Temp List View*/
    LinearLayout tempNotExistNone;
    LinearLayout tempExistListView;

    TempListViewModel tempListViewModel;
    TempListViewAdapter tempAdapter;
    ListView tempListView;

    Boolean isTempExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /*DB Setting*/
        helper = new TempSqlOpenHelper(this, "tempList.db",null,1);
        tempDB = helper.getWritableDatabase();

        /*Temp List Adapter*/
        tempNotExistNone = findViewById(R.id.Linear_TempPanel_none);
        tempExistListView = findViewById(R.id.Linear_TempPanel_listView);
        tempAdapter = setTempAdapter(getBaseContext());
        tempListView = setListViewTemp("temp", tempAdapter, (Activity)this);
        listViewHeightSet(tempAdapter, tempListView);

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

    void setTempData(String tempValue, String tempTime, String tempDate) {
        long now = System.currentTimeMillis();
        Date dateOrigin = new Date(now);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - E");

        tempListViewModel = new TempListViewModel();
        tempListViewModel.setUserId(MainActivity.sharedPreferences.getInt("USER_ID", 0) );
        tempListViewModel.setTempValue(tempValue);
        tempListViewModel.setTime(tempTime);
        tempListViewModel.setDate(tempDate);
        tempListViewModel.setId(helper.InsertTempListDB(tempListViewModel));

        tempAdapter.addModel(tempListViewModel);
        tempAdapter.notifyDataSetChanged();

        listViewHeightSet(tempAdapter, tempListView);
    }


    /*List View Adapter*/
    TempListViewAdapter setTempAdapter(Context context) {
        TempListViewAdapter tempListViewAdapter = new TempListViewAdapter(context);
        Cursor cursor = null;
        int tempId;
        int userId;
        String tempValue;
        String tempTime;
        String tempDate;

        try {
            cursor = tempDB.query("TEMP_TABLE", null, null, null, null, null, null);
            if (cursor != null) {
                isTempExist = true;

                while (cursor.moveToNext()) {
                    tempId = cursor.getInt(cursor.getColumnIndex("TEMP_ID"));
                    userId = cursor.getInt(cursor.getColumnIndex("USER_ID"));
                    tempValue = cursor.getString(cursor.getColumnIndex("TEMP_VALUE"));

                }

            } else {
                isTempExist = false;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /*List View Setting */
    ListView setListViewTemp(String part, TempListViewAdapter tempListViewAdapter, Activity activity) {
        ListView listView;
        String layoutId = "list_view_"+part;
        int resID = getResources().getIdentifier(layoutId, "id", activity.getPackageName());
        listView = (ListView) findViewById(resID);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(tempListViewAdapter);
        return listView;
    }

    private static void listViewHeightSet(BaseAdapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }

}