package com.jwlks.localdblistview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.jwlks.localdblistview.Util.BaseDialog;
import com.jwlks.localdblistview.Util.OnSingleClickListener;
import com.jwlks.localdblistview.Util.showAlert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserListViewAdapter extends BaseAdapter {
    private ArrayList<UserListViewModel> userListViewModelArrayList = new ArrayList<UserListViewModel>();

    /*DB Setting*/
    SQLiteDatabase userDB;
    UserSqlOpenHelper helper = null;

    /*User Dialog*/
    BaseDialog baseDialogUserEdit;
    Button buttonNo;
    Button buttonYes;
    EditText editTextUserName;
    EditText editTextUserAge;
    String textUserDate;

    public UserListViewAdapter(Context context) {
        /*DB Setting*/
        helper = new UserSqlOpenHelper(context, "userList.db",null,1);
        userDB = helper.getWritableDatabase();
        helper.SearchUserListDB();
    }

    @Override
    public int getCount() {
        return userListViewModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return userListViewModelArrayList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.listitem_account, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView profile = (ImageView) view.findViewById(R.id.profile_image) ;
        TextView name = (TextView) view.findViewById(R.id.user_name) ;
        TextView age = (TextView) view.findViewById(R.id.user_age) ;
        TextView date = (TextView) view.findViewById(R.id.user_regist_date) ;
        FrameLayout background = view.findViewById(R.id.list_background);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        UserListViewModel userListViewModel = userListViewModelArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        profile.setImageDrawable(userListViewModel.getProfile());
        name.setText(userListViewModel.getName());
        age.setText(userListViewModel.getAge());
        date.setText(userListViewModel.getDate());

        if(MainActivity.sharedPreferences.getInt("USER_ID",0) == position){
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.dotted_line_border_style00));
        }

        // 화면에 클릭할 수 있는 버튼 이벤트 정의
        Button buttonSelect = view.findViewById(R.id.button_UserPanel_select);
        Button buttonEdit = view.findViewById(R.id.button_UserPanel_edit);

        // 아이템 내 각 버튼 포지션 태그 정의
        buttonSelect.setTag(position);
        buttonEdit.setTag(position);

        // 온클릭 리스너 연결
        buttonSelect.setOnClickListener(onClickListenerSelect);
        buttonEdit.setOnClickListener(onClickListenerEdit);

        return view;
    }

    public void addItem(String name, String age, String date, Drawable profile) {

        UserListViewModel item = new UserListViewModel();
        item.setName(name);
        item.setAge(age);
        item.setDate(date);
        item.setProfile(profile);

        userListViewModelArrayList.add(item);
    }

    public void addModel(UserListViewModel model) {
        userListViewModelArrayList.add(model);
    }

    //Onclick Listener
    Button.OnClickListener onClickListenerSelect = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            Context context = v.getContext();
            int position = Integer.parseInt( (v.getTag().toString()) );

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            UserListViewModel userListViewModel = userListViewModelArrayList.get(position);

            Toast.makeText(context, "Position Select : " + position , Toast.LENGTH_SHORT).show();

            //*SharePreference
            SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
            editor.putBoolean("USER_ENABLE", true);
            editor.putInt("USER_ID", position);
            editor.putString("USER_NAME", userListViewModel.getName());
            editor.putString("USER_AGE", userListViewModel.getAge());
            editor.putString("USER_DATE", userListViewModel.getDate());
            editor.apply();
            //((Activity)context).finish();

        }
    };

    Button.OnClickListener onClickListenerEdit = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            Context context = v.getContext();
            int position = Integer.parseInt( (v.getTag().toString()) );
            Toast.makeText(context, "Position Edit : " + position , Toast.LENGTH_SHORT).show();
            set_dialogView_UserEdit(context, position);

        }
    };

    /*Dialog Setting*/
    void set_dialogView_UserEdit(Context context, int position) {

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        UserListViewModel userListViewModel = userListViewModelArrayList.get(position);

        //BaseDialog 연결
        /*Dialog Setting*/
        baseDialogUserEdit = new BaseDialog(context, R.layout.dialog_user_add);
        buttonNo = baseDialogUserEdit.findViewById(R.id.button_no);
        buttonYes = baseDialogUserEdit.findViewById(R.id.button_yes);
        editTextUserName = baseDialogUserEdit.findViewById(R.id.edit_user_name);
        editTextUserAge = baseDialogUserEdit.findViewById(R.id.edit_user_age);

        /*Dialog Setting*/
        editTextUserName.setText(userListViewModel.getName());
        editTextUserAge.setText(userListViewModel.getAge());
        textUserDate = userListViewModel.getDate();

        baseDialogUserEdit.show();

        /*Set Click Event*/
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                baseDialogUserEdit.dismiss(); // 다이얼로그 닫기
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editTextUserName.getText().toString();
                String userAge = editTextUserAge.getText().toString();
                String userDate = textUserDate;

                userListViewModel.setName(userName);
                userListViewModel.setAge(userAge);
                userListViewModel.setDate(userDate);
                userListViewModel.setProfile(ContextCompat.getDrawable(context, R.drawable.ic_user));

                helper.UpdateUserListDB(userListViewModel, Integer.toString(position + 1));
                userListViewModelArrayList.set(position, userListViewModel);

                notifyDataSetChanged();

                baseDialogUserEdit.dismiss(); // 다이얼로그 닫기

            }
        });

    }
}
