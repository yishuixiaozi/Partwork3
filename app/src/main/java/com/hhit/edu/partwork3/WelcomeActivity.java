package com.hhit.edu.partwork3;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
public class WelcomeActivity extends Activity {
    private static final int DELAY=2000;//延迟2秒
    private static final int GO_HOME=1000;
    private static final int GO_GUIDE=1001;
    private boolean IsFirst=false;
    private static final String START_KEY="isFirst";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();//初始化的作用
    }
    //去app登录界面
    private void gohome(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    private void goguide(){
        startActivity(new Intent(this,GuideActivity.class));
        finish();
    }
    private Handler handler=new Handler(){
       public void handleMessage(android.os.Message msg){
           switch (msg.what){
               case GO_HOME:
                   gohome();
                   break;
               case GO_GUIDE:
                   goguide();
                   break;
           }
       }
    };
    /**
     * 初始化preferences的值判断去哪个界面处理
     */
    private void init(){
        SharedPreferences preferences=getSharedPreferences("mydatabase",MODE_PRIVATE);
        //只访问本程序的perferences
        IsFirst=preferences.getBoolean(START_KEY,true);
        //检索不到start_key的值，就返回true
        if (!IsFirst){//不是第一次登陆
            handler.sendEmptyMessageDelayed(GO_HOME,DELAY);//延缓两秒发送数据
        }else {
            handler.sendEmptyMessageDelayed(GO_GUIDE,DELAY);//启动引导页
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean(START_KEY,false);
            //editor.clear();
            editor.commit();
        }
    }
}
