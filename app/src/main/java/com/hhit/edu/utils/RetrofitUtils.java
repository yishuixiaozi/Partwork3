package com.hhit.edu.utils;

import android.net.Uri;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 93681 on 2018/4/1.
 */

public class RetrofitUtils {
    private static final int READ_TIMEOUT=60;//读取超时时间60秒
    private static final int CONN_TIMEOUT=12;//连接超时时间12秒
    private static Retrofit mRetrofit;
    private RetrofitUtils(){
    }
    public static Retrofit newInstence(String url){
        mRetrofit=null;
        mRetrofit=new Retrofit.Builder()
                .baseUrl(url)//使用电脑配置
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
      return mRetrofit;
    }
}
