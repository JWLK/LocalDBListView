package com.jwlks.localdblistview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListViewAdapter extends BaseAdapter {
    private ArrayList<UserListViewModel> userListViewModelArrayList = new ArrayList<UserListViewModel>();

    public UserListViewAdapter() {

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
        final int pos = position;
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

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        UserListViewModel userListViewModel = userListViewModelArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        profile.setImageDrawable(userListViewModel.getProfile());
        name.setText(userListViewModel.getName());
        age.setText(userListViewModel.getAge());
        date.setText(userListViewModel.getDate());

        return view;
    }

    public void addItem(Drawable profile, String name, String age, String date) {
        UserListViewModel item = new UserListViewModel();

        item.setProfile(profile);
        item.setName(name);
        item.setAge(age);
        item.setDate(date);

        userListViewModelArrayList.add(item);
    }


}
