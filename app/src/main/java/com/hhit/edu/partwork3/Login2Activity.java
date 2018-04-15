package com.hhit.edu.partwork3;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class Login2Activity extends AppCompatActivity implements View.OnClickListener{

    private Tencent mTencent;
    private UserInfo userInfo;
    private BaseUiListener listener=new BaseUiListener();
    private Button loginQQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        loginQQ= (Button) findViewById(R.id.loginQQ);
        loginQQ.setOnClickListener(this);
        mTencent=Tencent.createInstance("1106841364",this.getApplicationContext());
    }

  /* private Handler mHandler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           if (msg.what==0){
               JSONObject response = (JSONObject) msg.obj;
               if (response.has("nickname")) {
                   Gson gson=new Gson();
                   System.out.println("返回的值的测试看看是什么东西"+response.toString());

           }
       }
   };*/
       @Override
       public void onClick(View v) {
           switch (v.getId()){
               case R.id.loginQQ:
                   mTencent.login(this,"all",listener);
                   break;
           }
       }
       class BaseUiListener implements IUiListener{
        @Override
        public void onComplete(Object o) {
            System.out.println("-----------------登录成功----------------"+o.toString());
            try{
                JSONObject jsonObject=new JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
           /**
            * 利用传递回来的授权信息才能够进一步访问用户详细信息
            */
        public void initOpenidAndToken(JSONObject jsonObject){
            try{
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                        && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
           /**
            * 获取用户详细信息
            */
        public void updateUserInfo(){
            if (mTencent!=null&&mTencent.isSessionValid()){
                IUiListener listener=new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        System.out.println("---success--userinfo---"+o.toString());
                    }
                    @Override
                    public void onError(UiError uiError) {
                        System.out.println("-----------------------uiError");
                    }
                    @Override
                    public void onCancel() {
                        System.out.println("-----------------------cancel");
                    }
                };
                userInfo=new UserInfo(Login2Activity.this,mTencent.getQQToken());
                userInfo.getUserInfo(listener);
            }
        }
        @Override
        public void onError(UiError uiError) {
            System.out.println("wrong=========================");
        }
        @Override
        public void onCancel() {
            System.out.println("wrong------------------------");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }
}
