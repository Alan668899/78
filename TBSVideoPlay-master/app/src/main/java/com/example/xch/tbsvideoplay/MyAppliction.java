package com.example.xch.tbsvideoplay;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

public class MyAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //回调接口初始化完成接口回调
        QbSdk.PreInitCallback pcb=new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("myApplication", " x5内核加载成功？" + b);
            }
        };

        //x5内核预加载，异步初始化x5 webview所需环境
        QbSdk.initX5Environment(getApplicationContext(), pcb);
    }
}
