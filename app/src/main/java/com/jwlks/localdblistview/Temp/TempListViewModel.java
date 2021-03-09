package com.jwlks.localdblistview.Temp;

import android.graphics.drawable.Drawable;

public class TempListViewModel {
    private int id ;
    private int userId ;
    private String tempValue ;
    private String date ;
    private String time ;

    public void setId(int id) {
        this.id = id ;
    }
    public void setUserId(int userId) {
        this.userId = userId ;
    }
    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }
    public void setTime(String time) {
        this.time = time ;
    }
    public void setDate(String date) {
        this.date = date ;
    }

    public int getId() {
        return this.id ;
    }
    public int getUserId() {
        return this.userId ;
    }
    public String getTempValue() {
        return this.tempValue;
    }
    public String getTime() {
        return this.time;
    }
    public String getDate() {
        return this.date ;
    }
}
