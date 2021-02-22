package com.jwlks.localdblistview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jwlks.localdblistview.Util.BaseDialog;

public class MainActivity extends AppCompatActivity {


    BaseDialog baseDialogUserAdd;
    Button buttonNo;
    Button buttonYes;

    /*DB Setting*/
    private UserSqlOpenHelper helper = null;

    /*UserList*/
    UserListViewAdapter userAdapter;
    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Dialog Setting*/
        baseDialogUserAdd = new BaseDialog(this, R.layout.dialog_user_add);

        buttonNo = baseDialogUserAdd.findViewById(R.id.button_no);
        buttonYes = baseDialogUserAdd.findViewById(R.id.button_yes);


        /*DB Setting*/
        helper = new UserSqlOpenHelper(this, "userList.db",null,1);

        /*User List Adapter*/
        userAdapter = setUserAdapter(getBaseContext());
        userListView = setListViewUser("user", userAdapter, (Activity)this);
        listViewHeightSet(userAdapter, userListView);


        buttonEvent();

    }

    void buttonEvent() {
        Button buttonUserAdd = findViewById(R.id.button_UserPanel_addUser);
        buttonUserAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialogView_UserAdd();
            }
        });
    }


    /*Dialog Setting*/
    void show_dialogView_UserAdd(){
        baseDialogUserAdd.show();

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                baseDialogUserAdd.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                baseDialogUserAdd.dismiss(); // 다이얼로그 닫기
            }
        });

    }



    /*Liset View Adapter*/
    UserListViewAdapter setUserAdapter(Context context){
        UserListViewAdapter userListViewAdapter = new UserListViewAdapter();
        userListViewAdapter.addItem(ContextCompat.getDrawable(context,
                R.drawable.ic_user),
                "David",
                "21",
                "2021.01.01");
        userListViewAdapter.addItem(ContextCompat.getDrawable(context,
                R.drawable.ic_user),
                "Paul",
                "23",
                "2021.01.03");
        userListViewAdapter.addItem(ContextCompat.getDrawable(context,
                R.drawable.ic_user),
                "Lay",
                "25",
                "2021.01.21");
        return userListViewAdapter;
    }
    /*List View Setting */

    ListView setListViewUser(String part, UserListViewAdapter userListViewAdapter, Activity activity) {
        ListView listView;
        String layoutId = "list_view_"+part;
        int resID = getResources().getIdentifier(layoutId, "id", activity.getPackageName());
        listView = (ListView) findViewById(resID);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(userListViewAdapter);
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