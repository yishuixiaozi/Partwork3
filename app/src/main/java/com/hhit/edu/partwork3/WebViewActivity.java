package com.hhit.edu.partwork3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hhit.edu.utils.IntentUtils;
import com.hhit.edu.utils.StatusUtils;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;//webView对象
    ProgressBar pb;//缓冲图形
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();//隐藏标题栏
        //设置沉浸式模式，就是连电量显示那一栏都是同样的样式被设置了
        //无导航栏，无标题栏
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
    }

    private boolean flag=true;//设置标记，避免重走该生命周期，重复设置

    /**
     * WebViewActivity在加载完毕的时候和退出的时候就执行这个内容
     * 一般情况下用来获取高度宽度等内容
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (flag){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                RelativeLayout rl= (RelativeLayout) findViewById(R.id.webView_titleLayout);
                int statusHeight= StatusUtils.getStatusHeight(this);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl.getLayoutParams();
                params.height += statusHeight;
                rl.setLayoutParams(params);
                rl.setPadding(0, statusHeight, 0, statusHeight);
            }
            flag=false;
        }
    }

    /**
     * 初始化webview页面内容
     */
    private void initView(){
        System.out.println("这里是WebViewActivity----init方法");
        webView = (WebView) findViewById(R.id.webView);
        pb = (ProgressBar) findViewById(R.id.webview_progressbar);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent=getIntent();//这个应该就是discover页面传过来的参数内容
        String url=intent.getStringExtra(IntentUtils.KEY_DISCOVERURL);//这里边就是discover_url

        webView.loadUrl(url);//这是新的url  是在上一个页面里边intent意图传递过来的内容，让它来进行执行操作

        /**
         * setWebChromeClient用来辅助显示相关加载控件的内容
         * onProgressClient  这个是加载进度变化的时候执行的方法
         */
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
            }
        });

        /**
         * 这个是设置调用的页面不打开默认的系统浏览器而是在webview里边浏览显示
         * onPageStarted  页面一加载的时候执行的方法
         * onPageFinished  页面加载完毕的时候执行的方法
         */
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                webView.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });


    }

    /**
     * 点击backspace可以返回上一个界面，而不是退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按返回时，看网页是否能返回
            if (webView.canGoBack()) {
                webView.goBack();
                //返回true webview自己处理
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void back(View view){
        finish();
    }
}
