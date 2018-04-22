package com.hhit.edu.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hhit.edu.bean.UserBean;
import com.hhit.edu.fragments.LoginFragment;
import com.hhit.edu.fragments.MyhomeFragment;
import com.hhit.edu.partwork3.LoginnewActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 93681 on 2018/4/22.
 */

public class MyBaseUIlistener implements IUiListener{
    private Tencent mTencent;
    private Context context;
    private UserInfo userInfo;
    private String str;
    private String usertype;
    private LoginFragment loginFragment=new LoginFragment();
    public MyBaseUIlistener(Context context){
        this.context=context;
    }

    public MyBaseUIlistener(Context context,String usertype){
        this.context=context;
        this.usertype=usertype;
    }

    @Override
    public void onComplete(Object o) {
        mTencent=Tencent.createInstance("1106841364",context);
        Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
        Log.e("=======", "response:" + o);
        JSONObject obj = (JSONObject) o;
        try{
            String openID=obj.getString("openid");
            str=openID;
            String accessToken=obj.getString("access_token");
            String expires=obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken,expires);
            QQToken qqToken=mTencent.getQQToken();
            userInfo=new UserInfo(context,qqToken);
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    System.out.println("登录成功-----"+o.toString());
                    if (o==null){
                        return;
                    }
                    try {
                        JSONObject jo= (JSONObject) o;
                        String img=jo.getString("figureurl_qq_1");
                        loginFragment.getUserInfo(o,str,usertype);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(UiError uiError) {
                    System.out.println("登录失败");
                }

                @Override
                public void onCancel() {
                    System.out.println("登录取消");
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onError(UiError uiError) {
        System.out.println("授权失败");
    }
    @Override
    public void onCancel() {
        System.out.println("授权取消");
    }
}
