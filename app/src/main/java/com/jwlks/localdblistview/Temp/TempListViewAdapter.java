package com.jwlks.localdblistview.Temp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jwlks.localdblistview.R;

import java.util.ArrayList;

public class TempListViewAdapter extends BaseAdapter {
    private ArrayList<TempListViewModel> tempListViewModelArrayList = new ArrayList<TempListViewModel>();

    /*DB Setting*/
    SQLiteDatabase tempDB;
    TempSqlOpenHelper helper = null;

    public TempListViewAdapter(Context context) {
        /*DB Setting*/
        helper = new TempSqlOpenHelper(context, "tempList.db",null,1);
        tempDB = helper.getWritableDatabase();
        helper.SearchTempListDB();
    }

    @Override
    public int getCount() {
        if(tempListViewModelArrayList.size() < 3) {
            return tempListViewModelArrayList.size();
        } else {
            return 3;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return tempListViewModelArrayList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.listitem_temp, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView temp_type = (TextView) view.findViewById(R.id.temp_type) ;
        TextView temp_value = (TextView) view.findViewById(R.id.temp_value) ;
        TextView temp_time = (TextView) view.findViewById(R.id.temp_time) ;
        TextView temp_date = (TextView) view.findViewById(R.id.temp_date) ;

        // 온도 타입 정의
        String tempType = null;
        double dataDouble = 0.0;
        double minTempNormal = (float) 32.0; // 32.0
        double maxTempNormal = 37.2; // 37.2

        double minTempMildFever = maxTempNormal + 0.1;

        double alertTemp = 38.2; // 38.2
        double minTempHighFever = alertTemp + 0.1;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TempListViewModel tempListViewModel = tempListViewModelArrayList.get(position);

        dataDouble = Double.parseDouble(tempListViewModel.getTempValue());

        if( dataDouble >= minTempNormal && dataDouble <= maxTempNormal ) {
            tempType = "Normal"; // SET LANGUAGE SELECT CODE // Lang.getStringByLocal(contextMain, R.string.temp_info_ready, MainActivity.langCode)
            temp_type.setTextColor(ContextCompat.getColor(context, R.color.colorNormal));
            temp_value.setTextColor(ContextCompat.getColor(context, R.color.colorNormal));
        } else if(dataDouble >= minTempMildFever && dataDouble <= alertTemp ) {
            tempType = "Mild Fever";//Lang.getStringByLocal(contextMain, R.string.temp_info_ready, MainActivity.langCode)\
            temp_type.setTextColor(ContextCompat.getColor(context, R.color.colorMildFever));
            temp_value.setTextColor(ContextCompat.getColor(context, R.color.colorMildFever));
        } else if(dataDouble >= minTempHighFever) {
            tempType = "High Fever";
            temp_type.setTextColor(ContextCompat.getColor(context, R.color.colorHighFever));
            temp_value.setTextColor(ContextCompat.getColor(context, R.color.colorHighFever));
        }

        // 아이템 내 각 위젯에 데이터 반영
        temp_type.setText(tempType);
        temp_value.setText(tempListViewModel.getTempValue() + "°C");
        temp_time.setText(tempListViewModel.getTime());
        temp_date.setText(tempListViewModel.getDate());
        return view;
    }

    public void addItem(int id, int userId, String tempValue, String time, String date) {

        TempListViewModel item = new TempListViewModel();
        item.setId(id);
        item.setUserId(userId);
        item.setTempValue(tempValue);
        item.setTime(time);
        item.setDate(date);

        tempListViewModelArrayList.add(item);
    }

    public void addModel(TempListViewModel model) {
        tempListViewModelArrayList.add(model);
    }

    public void sortIdAsc(){
        tempListViewModelArrayList.sort(TempSort.idAsc);
        notifyDataSetChanged();
    }

    public void sortIdDesc(){
        tempListViewModelArrayList.sort(TempSort.idDesc);
        notifyDataSetChanged();
    }

}
