package com.hhit.edu.utils;

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
      /*  OkHttpClient client=new OkHttpClient();
        client.setReadTimeout*/
      /*  OkHttpClient client=new OkHttpClient();
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.MINUTES);
        client.setConnectTimeout(CONN_TIMEOUT,TimeUnit.MINUTES);*/
        mRetrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
      return mRetrofit;
    }
}
