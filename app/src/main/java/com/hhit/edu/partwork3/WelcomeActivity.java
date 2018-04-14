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
    //去app应用界面
    private void gohome(){
        System.out.println("去主页面判断------gologin");
        startActivity(new Intent(this,TestActivity.class));
        finish();
    }
    private void goguide(){
        System.out.println("去引导界面判断------goguide");
        startActivity(new Intent(this,GuideActivity.class));
        finish();
    }
    private Handler handler=new Handler(){
       public void handleMessage(android.os.Message msg){
           switch (msg.what){
               case GO_HOME:
                   System.out.println("直接去主界面，然后判断是否登录");
                   gohome();
                   break;
               case GO_GUIDE:
                   System.out.println("直接去引导界面，然后判断是否登录");
                   goguide();
                   break;
           }
       }
    };
    /**
     * 初始化preferences的值判断去哪个界面处理
     */
    private void init(){
        /* SharedPreferences preferences=getPreferences("mydatabase",MODE_PRIVATE);
         SharedPreferences preferences1=getSharedPreferences("mydatabase",MODE_PRIVATE);
         */
        SharedPreferences preferences=getSharedPreferences("mydatabase",MODE_PRIVATE);
        //只访问本程序的perferences
        IsFirst=preferences.getBoolean(START_KEY,true);
        //检索不到start_key的值，就返回true
        if (!IsFirst){//不是第一次登陆
            System.out.println("不是第一登录");
            handler.sendEmptyMessageDelayed(GO_HOME,DELAY);//延缓两秒发送数据
        }else {
            System.out.println("是第一次登陆");
            handler.sendEmptyMessageDelayed(GO_GUIDE,DELAY);//启动引导页
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean(START_KEY,false);
            //editor.clear();
            editor.commit();
        }
    }
}
