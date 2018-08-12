package com.example.test.mytest.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by zdk on 2018/3/21.
 */

public class PermissionUtils {

    public final static int REQUEST_CALL_PHONE = 10;
    public final static int REQUEST_CAMERA = 20;
    public final static int REQUEST_CHANGE_NETWORK_STATE = 30;
    public final static int REQUEST_CHANGE_WIFI_STATE = 40;
    public final static int REQUEST_EXTERNAL_STRONGE = 50;
    public final static int REQUEST_INSTALL_PACKAGES = 60;
    public final static int REQUEST_INSTALL_SHORTCUT = 70;
    public final static int REQUEST_LOCATION = 80;
    public final static int REQUEST_MODIFY_PHONE_STATE = 90;
    public final static int REQUEST_READ_CALL_LOG = 100;
    public final static int REQUEST_READ_CONTACTS = 110;
    public final static int REQUEST_READ_LOGS = 120;
    public final static int REQUEST_READ_PHONE_STATE = 130;
    public final static int REQUEST_READ_SMS = 140;
    public final static int REQUEST_RECORD_AUDIO = 150;
    public final static int REQUEST_SEND_SMS = 160;
    public final static int REQUEST_WRITE_CONTACTS = 170;
    public final static int REQUEST_WRITE_SETTINGS = 180;
//    public final static int  REQUEST_LOCATION_AND_STORAGE=12330;
//    public final static int  REQUEST_LOCATION_ONLY=12340;

    public static boolean getCallPhonePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CALL_PHONE);
            return false;
        } else return true;
    }

    public static boolean getCameraPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CAMERA);
            return false;
        } else return true;
    }

    public static boolean getChangeNetworkStatePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.CHANGE_NETWORK_STATE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CHANGE_NETWORK_STATE);
            return false;
        } else return true;
    }

    public static boolean getChangeWifiStatePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.CHANGE_WIFI_STATE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CHANGE_WIFI_STATE);
            return false;
        } else return true;
    }

    public static boolean getExternalStrongePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_EXTERNAL_STRONGE);
            return false;
        } else return true;
    }

    public static boolean getInstallPackagesPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.INSTALL_PACKAGES};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_INSTALL_PACKAGES);
            return false;
        } else return true;
    }

    public static boolean getInstallShortcutPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.INSTALL_SHORTCUT};
        if (ContextCompat.checkSelfPermission(activity,permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,permissions, REQUEST_INSTALL_SHORTCUT);
            return false;
        } else return true;
    }

    // 定位判断，判断精确GPS定位，去除原有基站模糊定位
    public static boolean getLocationPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            int i = ContextCompat.checkSelfPermission(activity, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_LOCATION);
                return false;
            } else {
                return true;
            }
        } else
            return true;
    }
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//            return false;
//        } else {
//            return true;
//        }
//    }

//    原权限判断，判断基站模糊定位和GPS精确定位
//    public static boolean getLocationPermissions(Activity activity) {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
//            return false;
//        } else {
//            return true;
//        }
//    }

    public static boolean getModifyPhoneStatePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.MODIFY_PHONE_STATE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_MODIFY_PHONE_STATE);
            return false;
        } else return true;
    }

    public static boolean getReadCallLogPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_CALL_LOG};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_CALL_LOG);
            return false;
        } else return true;
    }


    public static boolean getReadContactsPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_CONTACTS);
            return false;
        } else return true;
    }

    public static boolean getReadLogsPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_LOGS};
        if (ContextCompat.checkSelfPermission(activity,permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,permissions, REQUEST_READ_LOGS);
            return false;
        } else return true;
    }

    public static boolean getReadPhoneStatePermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_PHONE_STATE);
            return false;
        } else return true;
    }

    public static boolean getReadSMSPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_SMS};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_SMS);
            return false;
        } else return true;
    }

    public static boolean getRecordAudioPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_RECORD_AUDIO);
            return false;
        } else return true;
    }

    public static boolean getSendSMSPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.SEND_SMS};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_SEND_SMS);
            return false;
        } else return true;
    }

    public static boolean getWriteSettingsPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.WRITE_SETTINGS};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_WRITE_SETTINGS);
            return false;
        } else return true;
    }

    public static boolean getWriteContactsPermissions(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS};
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_WRITE_CONTACTS);
            return false;
        } else return true;
    }


}

