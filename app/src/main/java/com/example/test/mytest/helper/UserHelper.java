package com.example.test.mytest.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/8/9.
 */
public class UserHelper {
    public static void showToast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);// 显示时间较长
        toast.show();
    }

    //设置系统栏的颜色
    public static void setWindowStatusBarColor(Activity context, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window =context.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(context.getResources().getColor(color));
                //window.setNavigationBarColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
