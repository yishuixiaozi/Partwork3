package com.hhit.me.application;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wcy on 2016/4/3.
 */
public class ExpressApplication extends Application {
    private static ExpressApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);//这个是地图的内容
        SDKInitializer.setCoordType(CoordType.BD09LL);
        sInstance = this;//这个是返回快递的所有实例的
    }
    public static ExpressApplication getInstance() {
        return sInstance;
    }
}
