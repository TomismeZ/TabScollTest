package com.example.test.mytest.utils;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
