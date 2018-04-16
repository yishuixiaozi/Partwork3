package com.hhit.edu.partwork3;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //登录界面的必要参数
    private EditText loginId;
    private EditText loginPassword;
    private Button loginBtn;
    private Button loginMissps;
    private Button loginNewUser;
    private Button loginChangePw;
    //这个用于测试内容
    private Button loginqq;

    private Tencent mTencent;

    private String openidString;
    private UserInfo info;
    private UserBean userBean=new UserBean();
    private BaseUiListener baseUiListene=new BaseUiListener();//是这个地方我写错了，没有实例化
    Map<String, String> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//隐藏标题栏的作用
        mTencent=Tencent.createInstance("1106841364",this.getApplicationContext());
        intiView();
    }
    /**
     * 获得登录界面的所有组件
     */
    private void intiView(){
        loginId = (EditText) findViewById(R.id.loginId);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        loginMissps = (Button) findViewById(R.id.loginMissps);
        loginMissps.setOnClickListener(this);
        loginNewUser = (Button) findViewById(R.id.loginNewUser);
        loginNewUser.setOnClickListener(this);
        loginChangePw = (Button) findViewById(R.id.loginChangePw);
        loginChangePw.setOnClickListener(this);
        //qq登录的测试使用
        loginqq= (Button) findViewById(R.id.loginqq);
        loginqq.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                //进行一个数据库的操作
                /*
                * 1.获取页面输入的值
                * 2.与服务器端数据库比较
                * 3.看看结果，然后是否跳转*/
                Toast.makeText(LoginActivity.this, "暂时没写方法呢",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.loginMissps:
                System.out.println("忘记密码");
                Intent a = new Intent(LoginActivity.this,
                        findpasswordActivity.class);
                startActivity(a);
                break;
            case R.id.loginNewUser:
                System.out.println("注册");
                Intent i = new Intent(LoginActivity.this, registerActivity.class);
                startActivity(i);
                break;
            case R.id.loginChangePw:
                System.out.println("更改密码");
                Intent l = new Intent(LoginActivity.this,
                        changepasswordActivity.class);
                startActivity(l);
                break;
            case R.id.loginqq:
                System.out.println("---------------------------------第三方qq登录");
                mTencent.login(LoginActivity.this,"all",baseUiListene);
                System.out.println("mtencent-----="+mTencent.getOpenId());
            default:
                break;
        }
    }
    //自定义一个内部类用来处理接口回调信息
    private class BaseUiListener implements IUiListener{
        @Override
        public void onComplete(Object o) {
            System.out.println("登录成功了-------------------------");
            //登录成功获取用户基本信息方法
            try {
                System.out.println("-------0.tostring--------"+o.toString());
                openidString=((JSONObject)o).getString("openid");//openid是腾讯返回的固定内容
                //给mTencent对象设置内容
                userBean.setUserid(((JSONObject)o).getString("openid"));
                mTencent.setOpenId(openidString);
                mTencent.setAccessToken(((JSONObject) o).getString("access_token"),((JSONObject) o).getString("expires_in"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            //进一步获取详细信息
            QQToken qqToken=mTencent.getQQToken();
            info=new UserInfo(getApplicationContext(),qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    System.out.println("用户信息已获取");
                    try{
                        System.out.println("-----------------------"+((JSONObject) o).getString("nickname")+((JSONObject) o).getString("gender"));
                        //Toast.makeText(getApplicationContext(), ((JSONObject) o).getString("nickname")+((JSONObject) o).getString("gender"), Toast.LENGTH_SHORT).show();
                        //Log.v("UserInfo",o.toString());
                        //userBean.setUserid(((JSONObject) o).getString("nickname"));
                        userBean.setNickname(((JSONObject) o).getString("nickname"));
                        userBean.setYear(((JSONObject) o).getString("year"));
                        userBean.setCity(((JSONObject) o).getString("city"));
                        userBean.setGender(((JSONObject) o).getString("gender"));
                        userBean.setPhotouri(((JSONObject) o).getString("figureurl_qq_2"));
                       /* map.put("nickname",((JSONObject) o).getString("nickname"));
                        map.put("year",((JSONObject) o).getString("year"));*/
                        saveInfo(userBean);
                        Intent intent=new Intent(LoginActivity.this, TestActivity.class);
                        startActivity(intent);
                        finish();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(UiError uiError) {
                    System.out.println("-----------------userinfo--error");
                }
                @Override
                public void onCancel() {
                    System.out.println("-----------------userinfo--cancel");
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("login-----------------------uiError--------");
        }
        @Override
        public void onCancel() {
            System.out.println("login-----------------------Cancel--------");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListene);
    }

    /**
     * 这个是进行自己服务器数据库的存储的内容
     * @param
     */
    private void saveInfo(UserBean userBean){
        System.out.println("------------saveInfo---"+map.get("nickname"));
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080/AndroidService/")//http://192.168.0.101 192.168.137.1
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        final HomePageInterface request=retrofit.create(HomePageInterface.class);
        request.UUserByUserid(userBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("success----------------------");
                    }
                });
    }
}
