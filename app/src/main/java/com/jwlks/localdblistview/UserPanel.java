package com.jwlks.localdblistview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jwlks.localdblistview.Util.BaseDialog;
import com.jwlks.localdblistview.Util.showAlert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPanel extends AppCompatActivity {

    Context context;

    /*DB Setting*/
    SQLiteDatabase userDB;
    UserSqlOpenHelper helper = null;

    /*User Dialog*/
    BaseDialog baseDialogUserAdd;
    Button buttonNo;
    Button buttonYes;
    EditText editTextUserName;
    EditText editTextUserAge;

    /*UserList*/
    UserListViewModel userListViewModel;
    UserListViewAdapter userAdapter;
    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_panel);

        context = this;

        /*DB Setting*/
        helper = new UserSqlOpenHelper(this, "userList.db",null,1);
        userDB = helper.getWritableDatabase();


        /*Dialog Setting*/
        baseDialogUserAdd = new BaseDialog(this, R.layout.dialog_user_add);
        buttonNo = baseDialogUserAdd.findViewById(R.id.button_no);
        buttonYes = baseDialogUserAdd.findViewById(R.id.button_yes);
        editTextUserName = baseDialogUserAdd.findViewById(R.id.edit_user_name);
        editTextUserAge = baseDialogUserAdd.findViewById(R.id.edit_user_age);


        /*User List Adapter*/
        userAdapter = setUserAdapter(getBaseContext());
        userListView = setListViewUser("user", userAdapter, (Activity)this);
        listViewHeightSet(userAdapter, userListView);


        buttonEvent(getBaseContext());

    }

    void buttonEvent(Context context) {
        Button buttonUserAdd = findViewById(R.id.button_UserPanel_addUser);
        buttonUserAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialogView_UserAdd(context);
            }
        });
    }


    /*Dialog Setting*/
    void show_dialogView_UserAdd(Context context){
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
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");

                String userName = editTextUserName.getText().toString();
                String userAge = editTextUserAge.getText().toString();
                String userDate = mFormat.format(date);

                userListViewModel = new UserListViewModel();
                userListViewModel.setName(userName);
                userListViewModel.setAge(userAge);
                userListViewModel.setDate(userDate);
                userListViewModel.setProfile(ContextCompat.getDrawable(context, R.drawable.ic_user));

                if(userAdapter.getCount() < 5){
                    if(editTextUserName.getText().toString().equals("") || editTextUserName.getText().toString() == null){
                        Toast.makeText(context, "Please Input User Name", Toast.LENGTH_SHORT).show();
                    } else if (editTextUserAge.getText().toString().equals("") || editTextUserAge.getText().toString() == null){
                        Toast.makeText(context, "Please Input User Age", Toast.LENGTH_SHORT).show();
                    } else {
                        helper.InsertUserListDB(userListViewModel);
                        userAdapter.addModel(userListViewModel);
                        userAdapter.notifyDataSetChanged();
                        listViewHeightSet(userAdapter, userListView);
                    }
                    baseDialogUserAdd.dismiss(); // 다이얼로그 닫기
                } else {
                    showAlert.set(context, "사용자를 추가할 수 없습니다.", "최대 등록 할 수 있는 사용자는 5명 입니다.");
                }
            }
        });

    }


    /*Liset View Adapter*/
    UserListViewAdapter setUserAdapter(Context context){
        UserListViewAdapter userListViewAdapter = new UserListViewAdapter(context);
        Cursor cursor = null;
        String userId;
        String userName;
        String userAge;
        String userDate;
        String userProfile;

        try {
            cursor = userDB.query("USER_TABLE", null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                    userName = cursor.getString(cursor.getColumnIndex("USER_NAME"));
                    userAge = cursor.getString(cursor.getColumnIndex("USER_AGE"));
                    userDate = cursor.getString(cursor.getColumnIndex("USER_DATE"));
                    userProfile = cursor.getString(cursor.getColumnIndex("USER_PROFILE"));
                    Drawable userProfileDrawable = ContextCompat.getDrawable(context, R.drawable.ic_user);
                    userListViewAdapter.addItem(
                            userName,
                            userAge,
                            userDate,
                            userProfileDrawable
                    );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


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