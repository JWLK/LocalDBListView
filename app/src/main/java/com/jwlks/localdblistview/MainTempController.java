package com.jwlks.localdblistview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jwlks.localdblistview.Temp.TempListViewAdapter;
import com.jwlks.localdblistview.Temp.TempListViewModel;
import com.jwlks.localdblistview.Temp.TempSqlOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class MainTempController {


    /**
     * INIT VALUE
     */
    /*Color */
    int colorWhite;

    /*DB Setting*/
    public static SQLiteDatabase tempDB;
    public static TempSqlOpenHelper tempHelper = null;

    /*Temp List View*/
    public static TempListViewAdapter tempAdapter = null;
    ListView tempListView;

    private AppCompatActivity appCompatActivity;

    MainTempController(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    void initialize() {
        getColorMaker();
        setXmlComponent();
        buttonEvent();
    }

    void getColorMaker() {
        //colorWhite = ContextCompat.getColor(appCompatActivity, R.color.white);
    }

    void setXmlComponent() {
        /*DB Setting*/
        tempHelper = new TempSqlOpenHelper(appCompatActivity, "tempList.db",null,1);
        tempDB = tempHelper.getWritableDatabase();

        /*Temp List Adapter*/
        tempAdapter = setTempAdapter(appCompatActivity, tempDB);
        tempListView = setListViewTemp("temp", tempAdapter, appCompatActivity);
        tempAdapter.sortIdDesc();
    }

    void buttonEvent() {
        /*Dev Test Data Setting*/
        Button buttonDataMeasuring = appCompatActivity.findViewById(R.id.button_data_measuring);
        buttonDataMeasuring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempAdapter.getCount() == 0){
                    TempListViewShowControl(appCompatActivity,true);
                }
                AddTempData("24.5", tempAdapter, tempHelper);
            }
        });

        Button buttonDataNormal = appCompatActivity.findViewById(R.id.button_data_normal);
        buttonDataNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempAdapter.getCount() == 0){
                    TempListViewShowControl(appCompatActivity,true);
                }
                AddTempData("36.5", tempAdapter, tempHelper);
            }
        });

        Button buttonDataMild = appCompatActivity.findViewById(R.id.button_data_mild);
        buttonDataMild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempAdapter.getCount() == 0){
                    TempListViewShowControl(appCompatActivity,true);
                }

                AddTempData("37.6", tempAdapter, tempHelper);
            }
        });

        Button buttonDataHigh = appCompatActivity.findViewById(R.id.button_data_high);
        buttonDataHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempAdapter.getCount() == 0){
                    TempListViewShowControl(appCompatActivity,true);
                }
                AddTempData("38.4", tempAdapter, tempHelper);
            }
        });

    }

    static void AddTempData(String tempValue,TempListViewAdapter tempListViewAdapter, TempSqlOpenHelper tempSqlOpenHelper) {
        TempListViewModel tempModel = setTempData(tempValue, tempSqlOpenHelper);
        tempListViewAdapter.addModel(tempModel);
        tempListViewAdapter.sortIdDesc();
    }

    /*Lise View Control Function*/
    static void TempListViewShowControl(AppCompatActivity appCompatActivity, Boolean isExist) {
        LinearLayout tempNotExistNone = appCompatActivity.findViewById(R.id.Linear_TempPanel_none);;
        LinearLayout tempExistListView = appCompatActivity.findViewById(R.id.Linear_TempPanel_listView);;
        if(!isExist) {
            tempNotExistNone.setVisibility(View.VISIBLE);
            tempExistListView.setVisibility(View.GONE);
        } else {
            tempNotExistNone.setVisibility(View.GONE);
            tempExistListView.setVisibility(View.VISIBLE);
        }
    }

    /*Temp Data Add function*/
    static TempListViewModel setTempData(String tempValue, TempSqlOpenHelper tempSqlOpenHelper) {
        TempListViewModel tempListViewModel = new TempListViewModel();
        long now = System.currentTimeMillis();
        Date dateOrigin = new Date(now);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", new Locale("en", "US"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd EE", new Locale("en", "US"));

        String tempTime = timeFormat.format(dateOrigin);
        String tempDate = dateFormat.format(dateOrigin);

        tempListViewModel.setUserId(MainActivity.sharedPreferences.getInt("USER_ID", 0) );
        tempListViewModel.setTempValue(tempValue);
        tempListViewModel.setTime(tempTime);
        tempListViewModel.setDate(tempDate);
        tempListViewModel.setId(tempSqlOpenHelper.InsertTempListDB(tempListViewModel));

        return tempListViewModel;
    }


    /*List View Adapter*/
    static TempListViewAdapter setTempAdapter(AppCompatActivity appCompatActivity, SQLiteDatabase tempDB) {
        TempListViewAdapter tempListViewAdapter = new TempListViewAdapter(appCompatActivity);
        Cursor cursor = null;
        int tempId;
        int userId;
        String tempValue;
        String tempTime;
        String tempDate;
        Boolean isTempExist = false;

        try {
            cursor = tempDB.query("TEMP_TABLE", null, null, null, null, null, null);
            Log.d("TEMP_TABLE", "cursor : " + cursor.getCount());
            if (cursor.getCount() > 0) {
                isTempExist = true;
                while (cursor.moveToNext()) {
                    tempId = cursor.getInt(cursor.getColumnIndex("TEMP_ID"));
                    userId = cursor.getInt(cursor.getColumnIndex("USER_ID"));
                    tempValue = cursor.getString(cursor.getColumnIndex("TEMP_VALUE"));
                    tempTime = cursor.getString(cursor.getColumnIndex("TEMP_TIME"));
                    tempDate = cursor.getString(cursor.getColumnIndex("TEMP_DATE"));
                    tempListViewAdapter.addItem(
                            tempId,
                            userId,
                            tempValue,
                            tempTime,
                            tempDate
                    );
                }
            } else {
                isTempExist = false;
            }
            TempListViewShowControl(appCompatActivity, isTempExist);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        TempListViewShowControl(appCompatActivity, isTempExist);
        return tempListViewAdapter;
    }

    /*List View Setting */
    ListView setListViewTemp(String part, TempListViewAdapter tempListViewAdapter, Activity activity) {
        ListView listView;
        String layoutId = "list_view_"+part;
        int resID = appCompatActivity.getResources().getIdentifier(layoutId, "id", activity.getPackageName());
        listView = (ListView) appCompatActivity.findViewById(resID);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(tempListViewAdapter);
        return listView;
    }

    private static void listViewHeightSet(BaseAdapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }
}
