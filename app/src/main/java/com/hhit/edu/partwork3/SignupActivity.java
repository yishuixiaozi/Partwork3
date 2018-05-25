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
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.SignupBean;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";

    List<SignupBean> signupdata;
    AbstractBaseAdapter<SignupBean> adapter;
    ImageView im_back;
    String myuserid;
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名userid---------测试"+myuserid);
        if (myuserid.equals("default")){
            Toast.makeText(this,"您的userid为空，请登陆",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginforestActivity.class));
        }else {
            setContentView(R.layout.activity_signup);
            getData();
            initdata();
            setupView();
        }

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
    //获取个人的报名数据-userid
    public void getData(){
        final SignupPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(SignupPageinterface.class);
        request.getAllSignup(myuserid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<SignupBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Toast.makeText(SignupActivity.this,"正在获取数据...",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(@NonNull ListResponse<SignupBean> signupBeanListResponse) {
                        signupdata.clear();//清空数据
                        signupdata.addAll(signupBeanListResponse.getItems());//为什么是添加？
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(SignupActivity.this,"获取数据错误",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initdata(){
        signupdata=new ArrayList<>();
        adapter=new AbstractBaseAdapter<SignupBean>(this,signupdata,R.layout.home_listview_mycontent){
            @Override
            public void bindData(int position, ViewHolder holder) {
                SignupBean signupBean=signupdata.get(position);
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(getApplication()).load(signupBean.getJobimageuri()).into(iv); //图形赋值成功
                TextView tv_title= (TextView) holder.findViewById(R.id.home_listview_title);
                tv_title.setText(signupBean.getTitle());
                TextView tv_paymoney= (TextView) holder.findViewById(R.id.home_listview_paymoney);
                tv_paymoney.setText(signupBean.getPaymoney());
                TextView tv_payway= (TextView) holder.findViewById(R.id.home_listview_payway);
                tv_payway.setText(signupBean.getPayway());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.home_listview_worktime);
                tv_worktime.setText(signupBean.getWorktime());
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
        SignupBean signupBean=signupdata.get(position);
        Intent intent=new Intent(this, JobdetailsActivity.class);
        intent.putExtra("id",signupBean.getJobid());
        intent.putExtra("userid",signupBean.getUserid());
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
                new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                deletesignup(id);
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
     * 取消报名
     * @param id
     */
    public void deletesignup(int id){
        SignupBean signupBean=signupdata.get(id);
        int signupid=signupBean.getSignupid();
        //数据库连接进行删除，删除成功后getdata()进行数据刷新
        final SignupPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(SignupPageinterface.class);
        request.deleteSignup(signupid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        //这里重新获取数据
                        getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("取消报名失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
