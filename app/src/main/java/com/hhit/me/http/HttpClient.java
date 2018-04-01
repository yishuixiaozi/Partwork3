package com.hhit.me.http;

import android.text.TextUtils;

/*import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;*/

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hhit.me.application.ExpressApplication;
import com.hhit.me.model.SearchResult;
import com.hhit.me.model.SuggestionResult;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
/*
import me.wcy.express.application.ExpressApplication;
import me.wcy.express.model.SearchResult;
import me.wcy.express.model.SuggestionResult;*/

/**
 * Created by hzwangchenyan on 2017/3/22.
 */
public class HttpClient {

    private static final String BASE_URL = "https://www.kuaidi100.com";
    private static final String HEADER_REFERER = "Referer";
    private static RequestQueue mRequestQueue;

    static {
        FakeX509TrustManager.allowAllSSL();
    }

    private static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //这里出现错误了
            System.out.println("test a test");
            mRequestQueue = Volley.newRequestQueue(ExpressApplication.getInstance().getApplicationContext());;
        }
        return mRequestQueue;
    }

    public static void query(String type, String postId, final HttpCallback<SearchResult> callback) {
        String action = "/query";
        Map<String, String> params = new HashMap<>(2);//这里边的2是空间因子
        params.put("type", type);//公司名：yuantong
        params.put("postid", postId);//快递单号:
        String url = makeUrl(action, params);

        GsonRequest<SearchResult> request = new GsonRequest<SearchResult>(url, SearchResult.class,
                new Response.Listener<SearchResult>() {
                    @Override
                    public void onResponse(SearchResult searchResult) {
                        callback.onResponse(searchResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError(volleyError);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(HEADER_REFERER, BASE_URL);
                return headers;
            }
        };
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    public static void getSuggestion(final String postId, final HttpCallback<SuggestionResult> callback) {
        String action = "/autonumber/autoComNum";
        Map<String, String> params = new HashMap<>(1);
        params.put("text", postId);
        String url = makeUrl(action, params);//https://www.kuaidi100.com/autonumber/autoComNum
        GsonRequest<SuggestionResult> request = new GsonRequest<SuggestionResult>(Request.Method.POST, url, SuggestionResult.class,
                new Response.Listener<SuggestionResult>() {
                    @Override
                    public void onResponse(SuggestionResult response) {
                        System.out.println("response="+response.toString());
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("出现错误了");
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(HEADER_REFERER, BASE_URL);
                return headers;
            }
        };
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    public static String urlForLogo(String logo) {
        String action = "/images/all/" + logo;
        return makeUrl(action, null);
    }

    private static String makeUrl(String action, Map<String, String> params) {
        String url = BASE_URL + action;
        //查看快递公司的：https://www.kuaidi100.com/autonumber/autoComNum
        //查看详细包裹内容的：https://www.kuaidi100.com/query?type=yuantong&postid=888664613925266116
        if (params == null || params.isEmpty()) {
            return url;
        }

        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                continue;
            }

            url += (i == 0) ? "?" : "&";
            url += (entry.getKey() + "=" + URLEncoder.encode(entry.getValue()));
            i++;
        }
        //https://www.kuaidi100.com/autonumber/autoComNum?text=888664613925266116  这是拼凑出来的结果
        //返回内容是{"comCode":"","num":"888664613925266116","auto":[{"comCode":"yuantong","id":"","noCount":6979502,"noPre":"8886","startTime":""}]}
        return url;
    }
}
