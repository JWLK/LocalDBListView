package com.jwlks.localdblistview.Account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Optional;

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
        String CREATE_USER_TABLE =
                "create table if not exists " + USER_TABLE + "(" +
                        USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_NAME + " TEXT NOT NULL , " +
                        USER_AGE + " TEXT NOT NULL , " +
                        USER_DATE + " TEXT NOT NULL , " +
                        USER_PROFILE + " TEXT NOT NULL " + ");";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createQuery = "DROP TABLE IF EXISTS " + USER_TABLE + ";";
        db.execSQL(createQuery);
    }


    public int InsertUserListDB(UserListViewModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_NAME", model.getName());
        contentValues.put("USER_AGE", model.getAge());
        contentValues.put("USER_DATE", model.getDate());
        contentValues.put("USER_PROFILE", model.getProfile().toString());
        long createUserId = getWritableDatabase().insert("USER_TABLE", null, contentValues);
        Log.d("DB_Insert", "id : " + createUserId);
        return Optional.ofNullable(createUserId).orElse(0L).intValue();
    }

    public void UpdateUserListDB(UserListViewModel model, String position) {
        String updateUserID[] = { position };

        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_NAME", model.getName());
        contentValues.put("USER_AGE", model.getAge());
        contentValues.put("USER_DATE", model.getDate());
        contentValues.put("USER_PROFILE", model.getProfile().toString());
        long createUserId = getWritableDatabase().update("USER_TABLE", contentValues, "USER_ID = ?", updateUserID);
        Log.d("DB_Update", "id : " + createUserId);
    }

    public void DeleteUserListDB(int userId) {
        String updateUserID[] = { Integer.toString(userId) };

        long createUserId = getWritableDatabase().delete("USER_TABLE","USER_ID = " + userId, null);
        Log.d("DB_Delete", "id : " + userId);
    }

    public void SearchUserListDB() {
        Cursor cursor = null;

        try {
            cursor = getWritableDatabase().query("USER_TABLE", null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                    String userName = cursor.getString(cursor.getColumnIndex("USER_NAME"));
                    String userAge = cursor.getString(cursor.getColumnIndex("USER_AGE"));
                    String userDate = cursor.getString(cursor.getColumnIndex("USER_DATE"));
                    String userProfile = cursor.getString(cursor.getColumnIndex("USER_PROFILE"));

                    Log.d("DBSearch", "USER_ID: " + userId + ", USER_NAME: " + userName + ", USER_AGE: " + userAge + ", USER_PROFILE: " + userProfile + ", USER_DATE: " + userDate);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
