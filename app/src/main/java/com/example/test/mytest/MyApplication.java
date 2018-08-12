package com.example.test.mytest;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2018/8/11.
 */

public class MyApplication extends Application {
    private static Context context;
    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
      SDKInitializer.initialize(getApplicationContext());//初始化百度地图
    }
}
