package com.iot.application2;

import android.app.Application;
import android.util.Log;

public class MyApp extends Application {
    private String id = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("test", "Application onCreate() called");
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
}
