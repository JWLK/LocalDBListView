package com.jwlks.localdblistview;

import android.graphics.drawable.Drawable;

public class UserListViewModel {
    private String name ;
    private String age ;
    private String date ;
    private Drawable profile ;

    public void setProfile(Drawable profile) {
        this.profile = profile ;
    }
    public void setName(String name) {
        this.name = name ;
    }

    public void setAge(String age) {
        this.age = age ;
    }
    public void setDate(String date) {
        this.date = date ;
    }


    public String getName() {
        return this.name ;
    }

    public String getAge() {
        return this.age ;
    }

    public String getDate() {
        return this.date ;
    }

    public Drawable getProfile() {
        return this.profile ;
    }
}
