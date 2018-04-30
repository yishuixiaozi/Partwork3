package com.hhit.edu.partwork3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.CollectionBean;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.fragments.MyhomeFragment;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.PullToRefreshHeadView;

import java.util.ArrayList;
import java.util.List;

import adapter.AbstractBaseAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";

    List<CollectionBean> collectiondata;
    AbstractBaseAdapter<CollectionBean> adapter;
    ImageView im_back;
    String userid;
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        userid=preferences.getString("userid","default");
        System.out.println("userid---------测试"+userid);
        if (userid.equals("default")){
            Toast.makeText(this,"您的userid为空，请登陆",Toast.LENGTH_SHORT).show();
        }else {
            setContentView(R.layout.activity_collection);
            getData();
            initdata();
            setupView();
        }

    }

    /**
     * 初始化组件内容
     */
    public void setupView(){
        im_back= (ImageView) findViewById(R.id.im_back);
        im_back.setOnClickListener(this);
        lv= (ListView) findViewById(R.id.collection_lv);
        setupRefreshView();
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        lv.setOnItemClickListener(this);//设置Item点击监听
    }

    /**
     * 初始化组件内容
     */
    public void setupRefreshView(){
        refresh= (PtrFrameLayout) findViewById(R.id.refresh);
        PullToRefreshHeadView pullHead=new PullToRefreshHeadView(this);
        //添加刷新tou
        refresh.setHeaderView(pullHead);
        //添加刷新头控制
        refresh.addPtrUIHandler(pullHead);
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("-----------------onRefreshLayout");
               // pagenum=0;
                getData();
            }
            //解决Listview与下拉刷新的冲突
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, lv, header);
            }
        });
    }


    /**
     * 获取数据
     */
    public void getData(){
        System.out.println("获取数据内容测试==============");
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        //获取用户id
        request.getAllCollection(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<CollectionBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Toast.makeText(CollectionActivity.this,"正在获取数据...",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(@NonNull ListResponse<CollectionBean> collectionBeanListResponse) {
                        collectiondata.clear();//清空数据
                        collectiondata.addAll(collectionBeanListResponse.getItems());//为什么是添加？
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(CollectionActivity.this,"获取数据错误",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 初始化数据
     */
    public void initdata(){
        collectiondata=new ArrayList<>();
        adapter=new AbstractBaseAdapter<CollectionBean>(this,collectiondata,R.layout.home_listview_mycontent) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                CollectionBean collectionBean=collectiondata.get(position);
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(getApplication()).load(collectionBean.getJobimageuri()).into(iv); //图形赋值成功
                TextView tv_title= (TextView) holder.findViewById(R.id.home_listview_title);
                tv_title.setText(collectionBean.getTitle());
                TextView tv_paymoney= (TextView) holder.findViewById(R.id.home_listview_paymoney);
                tv_paymoney.setText(collectionBean.getPaymoney());
                TextView tv_payway= (TextView) holder.findViewById(R.id.home_listview_payway);
                tv_payway.setText(collectionBean.getPayway());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.home_listview_worktime);
                tv_worktime.setText(collectionBean.getWorktime());
                //设置完毕
            }
        };
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.im_back:
               finish();//返回键的意思结束当前页面
               break;
           default:
               break;
       }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("一开始加载的时候显示内容");
        if (scrollState==0&&isAddMore){//一开始加载的时候显示内容，
            getData();
            buttonmore="1";
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {//就是说下拉看到最后一条了，设置isAddMore的属性为true，改变新的参数查询内容
            isAddMore = true;
        } else {
            isAddMore=false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("positon="+position);
        //将点击信息的ID传递过去显示详细内容
        CollectionBean collectionBean=collectiondata.get(position);
        Intent intent=new Intent(this, JobdetailsActivity.class);
        intent.putExtra("id",collectionBean.getJobid());
        intent.putExtra("userid",collectionBean.getUserid());
        startActivity(intent);

    }
}
