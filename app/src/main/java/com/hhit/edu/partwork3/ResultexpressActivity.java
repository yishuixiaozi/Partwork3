package com.hhit.edu.partwork3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hhit.me.http.HttpCallback;
import com.hhit.me.http.HttpClient;
import com.hhit.me.model.SearchInfo;
import com.hhit.me.model.SearchResult;
import com.hhit.me.utils.DataManager;
import com.hhit.me.viewholder.ResultViewHolder;
import com.hhit.me.widget.radapter.RAdapter;
import com.hhit.me.widget.radapter.RSingleDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultexpressActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG="ResultActivity";
    private ImageView ivLogo;
    private TextView tvPostId;
    private TextView tvName;
    private LinearLayout llResult;
    private RecyclerView rvResultList;
    private Button btnRemark;
    private LinearLayout llNoExist;
    private Button btnSave;
    private LinearLayout llError;
    private Button btnRetry;
    private TextView tvSearching;
    private SearchInfo searchInfo;

    private List<SearchResult.ResultItem> resultItemList = new ArrayList<>();
    private RAdapter<SearchResult.ResultItem> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultexpress);
        init();
        Intent intent=getIntent();
        searchInfo= (SearchInfo) intent.getSerializableExtra("searchinfo");
        System.out.println("name="+searchInfo.getName());//圆通速递
        //将图片动态填充到ivLogo里边https://www.kuaidi100.com/images/all/56/yuantong.png
        Glide.with(this)
                .load(HttpClient.urlForLogo(searchInfo.getLogo()))
                .dontAnimate()
                .placeholder(R.drawable.ic_default_logo)
                .into(ivLogo);
        refreshSearchInfo();//刷新搜索信息

        btnRemark.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnRetry.setOnClickListener(this);

        adapter=new RAdapter<>(resultItemList,new RSingleDelegate<>(ResultViewHolder.class));
        rvResultList.setLayoutManager(new LinearLayoutManager(this));
        rvResultList.setAdapter(adapter);

        query();//查询什么，估计查询内容
    }

    /**
     * 设置标题Logo,快递公司，快递公司单号
     */
    public void refreshSearchInfo(){
        String remark=DataManager.getInstance().getRemark(searchInfo.getPost_id());
        System.out.println("remark=----------------"+remark);
        if (TextUtils.isEmpty(remark)){
            tvName.setText(searchInfo.getName());//设置公司名字
            tvPostId.setText(searchInfo.getPost_id());//设置公司ID
        }
        else {
            tvName.setText(remark);
            tvPostId.setText(searchInfo.getName().concat(" ").concat(searchInfo.getPost_id()));
        }
    }

    /**
     * 这个方法我估计就是连接100的服务接口所需要的内容
     */
    public void query(){
        //这个就是HttpClient里边的查询的东西了，传递参数公司号，快递单号，执行回执内容
        HttpClient.query(searchInfo.getCode(), searchInfo.getPost_id(), new HttpCallback<SearchResult>() {
            @Override
            public void onResponse(SearchResult searchResult) {
                Log.i(TAG,searchResult.getMessage());
                onSearch(searchResult);//这里的searchResult就包含了所有的数据内容了
            }

            @Override
            public void onError(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage(),volleyError);
                llResult.setVisibility(View.GONE);
                llNoExist.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);//这个页面显示
                tvSearching.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 对返回结果进行查询
     * @param searchResult
     */
    private void onSearch(SearchResult searchResult){
        if (searchResult.getStatus().equals("200")){//status200就是成功返回的标志了
            llResult.setVisibility(View.VISIBLE);
            llNoExist.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
            tvSearching.setVisibility(View.GONE);

            Collections.addAll(resultItemList,searchResult.getData());

            adapter.notifyDataSetChanged();

            //设置搜索信息ischeck
            searchInfo.setIs_check(searchResult.getIscheck());
            //更新搜索历史
            DataManager.getInstance().updateHistory(searchInfo);
        }else {
            llResult.setVisibility(View.GONE);
            llNoExist.setVisibility(View.VISIBLE);//显示不存在的信息内容
            llError.setVisibility(View.GONE);
            tvSearching.setVisibility(View.GONE);
            btnSave.setText(DataManager.getInstance().idExists(searchInfo.getPost_id()) ? "运单备注" : "保存运单信息");
        }

    }
    /**
     * 初始化组件内容
     */
    public void init(){
        ivLogo= (ImageView) findViewById(R.id.iv_logo);//公司图标
        tvPostId= (TextView) findViewById(R.id.tv_post_id);//快递单号
        tvName= (TextView) findViewById(R.id.tv_name);//快递公司名
        llResult= (LinearLayout) findViewById(R.id.ll_result);//结果布局
        rvResultList= (RecyclerView) findViewById(R.id.rv_result_list);//结果列表
        btnRemark= (Button) findViewById(R.id.btn_remark);//充值按钮
        llNoExist= (LinearLayout) findViewById(R.id.ll_no_exist);//结果不存在布局
        btnSave= (Button) findViewById(R.id.btn_save);//保存按钮
        llError= (LinearLayout) findViewById(R.id.ll_error);//结果错误布局
        btnRetry= (Button) findViewById(R.id.btn_retry);//在此尝试按钮
        tvSearching= (TextView) findViewById(R.id.tv_searching);//正在加载物流信息

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_remark:
                remark();
                break;
            case R.id.btn_save:
                System.out.println("保存信息");
                break;
            case R.id.btn_retry:
                System.out.println("尝试按钮");
                break;
        }
    }

    private void remark(){
        System.out.println("这里是remark方法");
    }
}
