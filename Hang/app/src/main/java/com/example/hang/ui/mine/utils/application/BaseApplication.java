//package com.example.hang.ui.mine.utils.application;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.example.hang.BuildConfig;
//
//import org.xutils.x;
//
//import java.util.logging.Handler;
//
//public class BaseApplication extends Application {
//    public static Handler sHandler;
//    public static Context sContext = null;
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //初始化xUtils
//        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);
//
//    }
//}
