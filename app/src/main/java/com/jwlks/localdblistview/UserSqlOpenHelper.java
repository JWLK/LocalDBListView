package com.jwlks.localdblistview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserSqlOpenHelper extends SQLiteOpenHelper {
    private static final String USER_TABLE= "USER_TABLE";

    private static final String USER_ID  = "USER_ID";

    private static final String USER_NAME = "USER_NAME";

    private static final String USER_AGE = "USER_AGE";

    private static final String USER_DATE = "USER_DATE";

    private static final String USER_PROFILE = "USER_PROFILE";

    public UserSqlOpenHelper(@Nullable Context context,
                             @Nullable String name,
                             @Nullable SQLiteDatabase.CursorFactory factory,
                             int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CreateTable() {
        String CREATE_USER_TABLE =
                "create table if not exists " + USER_TABLE + "(" +
                        USER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_NAME + " TEXT NOT NULL , " +
                        USER_AGE + " TEXT NOT NULL , " +
                        USER_DATE + " TEXT NOT NULL , " +
                        USER_PROFILE + " TEXT NOT NULL " + ");";
        getWritableDatabase().execSQL(CREATE_USER_TABLE);
    }
}
