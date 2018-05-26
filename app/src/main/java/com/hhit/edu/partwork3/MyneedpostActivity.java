package com.hhit.edu.partwork3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.JobneedBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.fragments.FindemployeFragment2;
import com.hhit.edu.my_interface.JobneedPageinterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.PullToRefreshHeadView;

import java.util.ArrayList;
import java.util.List;

import adapter.AbstractBaseAdapter;
import cn.pedant.SweetAlert.SweetAlertDialog;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyneedpostActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";
    List<JobneedBean> jobneedBeandata;
    AbstractBaseAdapter<JobneedBean> adapter;
    ImageView im_back;
    String myuserid;
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myneedpost);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名userid---------测试"+myuserid);
        getData();
        initdata();
        setupView();
    }

    public void getData(){
        final JobneedPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(JobneedPageinterface.class);
        request.querymypost(myuserid)
              .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobneedBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ListResponse<JobneedBean> jobneedBeanListResponse) {
                        jobneedBeandata.clear();
                        jobneedBeandata.addAll(jobneedBeanListResponse.getItems());
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore="0";
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("wrong");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initdata(){
        jobneedBeandata=new ArrayList<>();
        adapter=new AbstractBaseAdapter<JobneedBean>(this,jobneedBeandata,R.layout.home_listview_myjobneedquery) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                JobneedBean jobneedBean=jobneedBeandata.get(position);
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(MyneedpostActivity.this).load(jobneedBean.getJobimageuri()).into(iv);
                TextView tv_title= (TextView) holder.findViewById(R.id.jobneed_title);
                tv_title.setText(jobneedBean.getJobneedtitle());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.jobneed_worktime);
                tv_worktime.setText(jobneedBean.getWorktime());
                TextView tv_bftime= (TextView) holder.findViewById(R.id.jobneed_bftime);
                tv_bftime.setText(jobneedBean.getBftime());
            }
        };
    }

    public void setupView(){
        im_back= (ImageView) findViewById(R.id.im_back);
        im_back.setOnClickListener(this);
        lv= (ListView) findViewById(R.id.signup_lv);
        registerForContextMenu(lv);
        setupRefreshView();
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        lv.setOnItemClickListener(this);//设置短按点击监听
    }

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

    /**
     * 传送到我的求职发布详情界面
     * 传递参数是jobneedid就是求职发布的信息的Id
     * @param parent
     * @param view
     * @param position 列表中元素位置
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("positon="+position);
        JobneedBean jobneedBean=jobneedBeandata.get(position);
        Intent intent=new Intent(this,JobneeddetailActivity.class);
        intent.putExtra("jobneedid",jobneedBean.getJobneedid());
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mymenu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int id=(int)info.id;
        switch (item.getItemId()) {
            case R.id.delete:
                //System.out.println("您点击的是---删除---位置是"+id);
                new SweetAlertDialog(MyneedpostActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定取消该兼职报名?")
                        .setContentText("该条兼职信息将随风而去")
                        .setCancelText("不，保留")
                        .setConfirmText("是，删除")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.setTitleText("取消")
                                        .setContentText("它依然在你身边")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deletemyjobneed(id);
                                sDialog.setTitleText("已删除")
                                        .setContentText("该条求职发布已删除")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
                break;
            case R.id.none:
                System.out.println("您点击的是---取消---位置是"+id);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 用于删除我的求职发布
     * @param id 这个是列表选中的第几个表示
     */
    public void deletemyjobneed(int id){
        JobneedBean jobneedBean=jobneedBeandata.get(id);
        System.out.println("------------测试jobneedid的值="+jobneedBean.getJobneedid());
        final JobneedPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(JobneedPageinterface.class);
        request.deletemypost(jobneedBean.getJobneedid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull String s) {
                        getData();//重新刷新列表内容
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("--------我的求职发布删除成功");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
}

