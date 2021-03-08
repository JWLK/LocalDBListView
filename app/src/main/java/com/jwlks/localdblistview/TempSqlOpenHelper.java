package com.jwlks.localdblistview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Optional;

public class TempSqlOpenHelper extends SQLiteOpenHelper {
    private static final String TEMP_TABLE= "TEMP_TABLE";

    private static final String TEMP_ID  = "TEMP_ID";

    private static final String USER_ID = "USER_ID";

    private static final String TEMP_VALUE = "TEMP_VALUE";

    private static final String TEMP_TIME = "TEMP_TIME";

    private static final String TEMP_DATE = "TEMP_DATE";

    public TempSqlOpenHelper(@Nullable Context context,
                             @Nullable String name,
                             @Nullable SQLiteDatabase.CursorFactory factory,
                             int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEMP_TABLE =
                "create table if not exists " + TEMP_TABLE + "(" +
                        TEMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_ID + " INTEGER NOT NULL , " +
                        TEMP_VALUE + " TEXT NOT NULL , " +
                        TEMP_TIME + " TEXT NOT NULL , " +
                        TEMP_DATE + " TEXT NOT NULL " + ");";
        db.execSQL(CREATE_TEMP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createQuery = "DROP TABLE IF EXISTS " + TEMP_TABLE + ";";
        db.execSQL(createQuery);
    }


    public int InsertTempListDB(TempListViewModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_ID", model.getUserId());
        contentValues.put("TEMP_VALUE", model.getTempValue());
        contentValues.put("TEMP_TIME", model.getTime());
        contentValues.put("TEMP_DATE", model.getDate());
        long createTempId = getWritableDatabase().insert("TEMP_TABLE", null, contentValues);
        Log.d("DB_Insert", "id : " + createTempId);
        return Optional.ofNullable(createTempId).orElse(0L).intValue();
    }

    public void UpdateTempListDB(TempListViewModel model, String position) {
        String updateTempID[] = { position };

        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_ID", model.getUserId());
        contentValues.put("TEMP_VALUE", model.getTempValue());
        contentValues.put("TEMP_TIME", model.getTime());
        contentValues.put("TEMP_DATE", model.getDate());
        long createTempId = getWritableDatabase().update("TEMP_TABLE", contentValues, "TEMP_ID = ?", updateTempID);
        Log.d("DB_Update", "id : " + createTempId);
    }

    public void DeleteTempListDB(int tempId) {
        String updateTempID[] = { Integer.toString(tempId) };

        long createTempId = getWritableDatabase().delete("TEMP_TABLE","TEMP_ID = " + tempId, null);
        Log.d("DB_Delete", "id : " + tempId);
    }

    public void SearchTempListDB() {
        Cursor cursor = null;

        try {
            cursor = getWritableDatabase().query("TEMP_TABLE", null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String tempId = cursor.getString(cursor.getColumnIndex("TEMP_ID"));
                    String userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                    String tempValue = cursor.getString(cursor.getColumnIndex("TEMP_VALUE"));
                    String tempTime = cursor.getString(cursor.getColumnIndex("TEMP_TIME"));
                    String tempDate = cursor.getString(cursor.getColumnIndex("TEMP_DATE"));

                    Log.d("DBSearch", "TEMP_ID: " + tempId + ", USER_ID: " + userId + ", TEMP_VALUE: " + tempValue + ", TEMP_TIME: " + tempTime + ", TEMP_DATE: " + tempDate);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
