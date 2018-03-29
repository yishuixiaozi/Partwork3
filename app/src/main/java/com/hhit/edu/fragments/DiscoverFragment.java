package com.hhit.edu.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hhit.edu.partwork3.R;
import com.hhit.edu.partwork3.WebViewActivity;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.IntentUtils;

/**
 * Created by 93681 on 2018/3/13.
 */

public class DiscoverFragment extends Fragment{
    /**
     * 变量img 和webView都是discover_fragment里边的内容
     */
    ImageView img;
    WebView webView;

    //这里应该是用来设置加载动画显示的内容了
    int startId=R.drawable.waiting_001;
    int endId=R.drawable.waiting_065;
    int imgCount=endId-startId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discover_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);//这个view应该就是discover_fragment了
    }

    private void initView(View view){
        webView= (WebView) view.findViewById(R.id.discover_webView);
        img= (ImageView) view.findViewById(R.id.discover_img);
        webView.getSettings().setJavaScriptEnabled(true);//使用javascript
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                int per=0;
                if (newProgress<100){
                    per=(int)(newProgress/(100f/imgCount));
                }else {
                    per=imgCount;
                }
                img.setImageResource(startId+per);//这个就是通过id的增加实现图片的更换，然后实现加载的效果
            }
        });
        /**
         * 动画开始和结束的动作，是他们消失
         */
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                img.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            /**
             * webView中的超链接被点击的时候，回调该方法
             * 这是回调函数，就是说将这个函数的指针传递给了WebViewActivity,它在某个
             * @param view   时候又执行了这个函数，并且在这个函数里边处理了相关的信息内容
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//在该webview里边点击其他的url的时候传递的url
                /*return super.shouldOverrideUrlLoading(view, url);*/
                System.out.println("DiscoverFragment-------------------shouldOverrideUrlLoading------");
                System.out.println("url="+url);
                Intent intent=new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra(IntentUtils.KEY_DISCOVERURL,url);//discover_url作为参数保存起来，值是url
                startActivity(intent);
                return true;

            }
        });
        //加载数据
        webView.loadUrl(ApiManager.DISCOVER_URL);
    }
}
