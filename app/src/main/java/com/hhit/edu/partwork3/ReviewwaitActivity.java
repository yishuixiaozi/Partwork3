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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.fragments.NoreviewFragment;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.my_interface.SignupPageinterface;
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

public class ReviewwaitActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";
    List<JobBean> jobdata;//实体数组内容
    AbstractBaseAdapter<JobBean> adapter;//<>这个是限制存储的数据的类型方式
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    String myuserid;
    ImageView im_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewwait);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名userid---------测试"+myuserid);
        getData();
        initdata();
        setupview();
    }

    public void getData(){
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobnoreview(myuserid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ListResponse<JobBean> jobBeanListResponse) {
                        jobdata.clear();
                        jobdata.addAll(jobBeanListResponse.getItems());//为什么是添加？
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(ReviewwaitActivity.this,"出现数据获取错误",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void initdata(){
        System.out.println("------noreview------------initdata()方法内容测试");
        jobdata=new ArrayList<>();
        adapter=new AbstractBaseAdapter<JobBean>(this,jobdata, R.layout.home_listview_mycontent) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                JobBean jobBean=jobdata.get(position);//一个一个的赋值的我认为
                //查询的数据赋值开始
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(getApplication()).load(jobBean.getJobimageuri()).into(iv); //图形赋值成功
                TextView tv_title= (TextView) holder.findViewById(R.id.home_listview_title);
                tv_title.setText(jobBean.getTitle());
                TextView tv_paymoney= (TextView) holder.findViewById(R.id.home_listview_paymoney);
                tv_paymoney.setText(jobBean.getPaymoney());
                TextView tv_payway= (TextView) holder.findViewById(R.id.home_listview_payway);
                tv_payway.setText(jobBean.getPayway());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.home_listview_worktime);
                tv_worktime.setText(jobBean.getWorktime());
                //设置完毕
            }
        };
    }

    public void setupview(){
        lv=(ListView) findViewById(R.id.home_lv);
        im_back= (ImageView) findViewById(R.id.im_back);
        registerForContextMenu(lv);
        setupRefreshView();
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        im_back.setOnClickListener(this);
        lv.setOnItemClickListener(this);//设置Item点击监听
    }

    public void setupRefreshView(){
        refresh= (PtrFrameLayout) findViewById(R.id.refresh);//获得可刷新对象
        PullToRefreshHeadView pullHead=new PullToRefreshHeadView(this);
        //添加刷新tou
        refresh.setHeaderView(pullHead);
        //添加刷新头控制
        refresh.addPtrUIHandler(pullHead);
        //设置刷新事件功能
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("-----------------onRefreshLayout");
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
                finish();
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
        JobBean jobBean=jobdata.get(position);
        System.out.println("--------jobBean.getId()"+jobBean.getId());
        Intent intent=new Intent(this,MypostdetailActivity.class);
        intent.putExtra("jobid",jobBean.getId());
        intent.putExtra("edityes","no");
        startActivity(intent);
    }

    /**
     * 长按提示框创建
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mymenu,menu);
    }
    /**
     * 长按提示框选项的选择
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int id=(int)info.id;
        switch (item.getItemId()) {
            case R.id.delete:
                System.out.println("您点击的是---删除---位置是"+id);
                new SweetAlertDialog(ReviewwaitActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                deletemypost(id);
                                sDialog.setTitleText("已删除")
                                        .setContentText("兼职信息离你而去")
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
     * 删除我的发布
     * @param id
     */
    public void deletemypost(int id){
        JobBean signupBean=jobdata.get(id);
        /*int signupid=signupBean.getSignupid();*/
        int jobid=signupBean.getId();
        //数据库连接进行删除，删除成功后getdata()进行数据刷新
        final SignupPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(SignupPageinterface.class);
        request.deleteByid(jobid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //Toast.makeText(MypostActivity.this,"获取数据中",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("信息获取错误");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
