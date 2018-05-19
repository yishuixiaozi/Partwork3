package com.hhit.edu.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.JobneedBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.HomeSecondView;
import com.hhit.edu.view.PullToRefreshHeadView;
import com.hhit.edu.view.ToorbarView;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 93681 on 2018/5/19.
 */

public class FindemployeFragment2 extends Fragment implements View.OnClickListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener,View.OnTouchListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";
    List<JobneedBean> jobneeddata;
    AbstractBaseAdapter<JobneedBean> needadapter;
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    String myuserid;
    boolean isExpand = false;
    LinearLayout view_head;
    EditText tvSearch;
    LinearLayout mSearchLayout;
    Toolbar toolbar;
    private TransitionSet mSet;
    ImageView iv_img;
    ToorbarView toorbarView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名userid---------测试"+myuserid);
        getData();
        initdata();
        toorbarView=new ToorbarView(getActivity());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_findemploye2, container,false);
        //inflater.inflate(R.layout.fragment_through, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupview(view);
    }

    /**
     * 获取数据要变化了
     */
    public void getData(){
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobneedBypage(pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobneedBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ListResponse<JobneedBean> jobneedBeanListResponse) {
                        if (buttonmore.equals("0")) {//判断每次刷新的标志，每次刷新清空data列表
                            jobneeddata.clear();
                        }
                        jobneeddata.addAll(jobneedBeanListResponse.getItems());
                        needadapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initdata(){
        jobneeddata=new ArrayList<>();
        needadapter=new AbstractBaseAdapter<JobneedBean>(getActivity(),jobneeddata,R.layout.home_listview_jobneed) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                JobneedBean jobneedBean=jobneeddata.get(position);
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(FindemployeFragment2.this).load(jobneedBean.getJobimageuri()).into(iv);
                TextView tv_title= (TextView) holder.findViewById(R.id.jobneed_title);
                tv_title.setText(jobneedBean.getJobneedtitle());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.jobneed_worktime);
                tv_worktime.setText(jobneedBean.getWorktime());
                TextView tv_bftime= (TextView) holder.findViewById(R.id.jobneed_bftime);
                tv_bftime.setText(jobneedBean.getBftime());
                TextView tv_myadvantage= (TextView) holder.findViewById(R.id.jobneed_myadvantage);
                tv_myadvantage.setText(jobneedBean.getMyadvantage());
            }
        };
    }

    public void setupview(View view){
        iv_img= (ImageView) view.findViewById(R.id.iv_img);
        view_head= (LinearLayout) view.findViewById(R.id.view_head);
        tvSearch= (EditText) view.findViewById(R.id.tv_search);
        mSearchLayout= (LinearLayout) view.findViewById(R.id.ll_search);
        toolbar= (Toolbar) view.findViewById(R.id.toolbar);
        //新增---设置为无色
        toolbar.getBackground().mutate().setAlpha(0);
        //scrollview滚动状态监听------去掉了
        tvSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER&&event.getAction()==event.ACTION_UP) {
                    System.out.println("你点击了搜索执行，输入值是"+tvSearch.getText().toString());
                    //pagenum=0;//重新设置为0，搜索的时候
                    //jobneeddata.clear();暂时不去清空数据
                    //参数内容的重新复制
                    //getData();这个地方在写一个getData2()专门用来处理关键字查询
                }
                return false;
            }
        });

        lv=(ListView) view.findViewById(R.id.home_lv);
        setupRefreshView(view);
        //lv.addHeaderView(R.drawable.night1);
        lv.addHeaderView(toorbarView);
        lv.setAdapter(needadapter);
        lv.setOnScrollListener(this);//设置监听方式
        tvSearch.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);
        tvSearch.setOnTouchListener(this);
        refresh.setOnTouchListener(this);
        lv.setOnTouchListener(this);
        lv.setOnItemClickListener(this);//设置Item点击监听
        lv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY=lv.getScrollY();
                System.out.println("-new   here ---"+scrollY);
            }
        });
        /*lv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scrollY1=v.getScrollY();
                System.out.println("--------test----"+scrollY1);
                if(scrollY<0){
                    toolbar.getBackground().mutate().setAlpha(0);
                    return;
                }
                //计算当前透明度比率
                System.out.println("----toor"+toolbar.getHeight()*1f);
                System.out.println("0---1212-"+toorbarView.getHeight());
                float radio= Math.min(1,scrollY/(toorbarView.getHeight()-toolbar.getHeight()*1f));
                System.out.println("-----------radio"+radio);
                //设置透明度
                toolbar.getBackground().mutate().setAlpha( (int)(radio * 0xFF));
                //设置toorbar
                if (scrollY >=toorbarView.getHeight() - toolbar.getHeight()  && !isExpand) {
                    expand();
                    isExpand = true;
                }
                //滚动距离<=0时 即滚动到顶部时  且当前伸展状态 进行收缩操作
                else if (scrollY<=0&& isExpand) {
                    reduce();
                    isExpand = false;
                }
            }
        });*/
    }

    public void setupRefreshView(View view){
        refresh= (PtrFrameLayout) view.findViewById(R.id.refresh);//获得可刷新对象
        PullToRefreshHeadView pullHead=new PullToRefreshHeadView(getContext());
        //添加刷新tou
        refresh.setHeaderView(pullHead);
        //添加刷新头控制
        refresh.addPtrUIHandler(pullHead);
        //设置刷新事件功能
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("-----------------onRefreshLayout");
                pagenum=0;
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("一开始加载的时候显示内容");
        if (scrollState==0&&isAddMore){//一开始加载的时候显示内容，
            pagenum+=8;
            getData();
            buttonmore="1";
            System.out.println("-----------------pagenum="+pagenum);
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

    private void changeViewhead(AbsListView view){
        if (view.getScrollY() >=view_head.getHeight() - toolbar.getHeight()  && !isExpand) {
            expand();
            isExpand = true;
        }
        //滚动距离<=0时 即滚动到顶部时  且当前伸展状态 进行收缩操作
        else if (view.getScrollY()<=0&& isExpand) {
            reduce();
            isExpand = false;
        }
    }
    private void changeToolbarAlpha(View view) {
        int scrollY = view.getScrollY();
        //快速下拉会引起瞬间scrollY<0
        if(scrollY<0){
            toolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前透明度比率
        float radio= Math.min(1,scrollY/(view_head.getHeight()-toolbar.getHeight()*1f));
        //设置透明度
        toolbar.getBackground().mutate().setAlpha( (int)(radio * 0xFF));
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private void expand() {
        //设置伸展状态时的布局
        //tvSearch.setText("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void reduce() {
        //设置收缩状态时的布局
        System.out.println("sethint");
        tvSearch.setHint("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = dip2px(80);
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    void beginDelayedTransition(ViewGroup view) {
        mSet = new AutoTransition();
        mSet.setDuration(300);
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    /**
     * 控制搜索框变化的内容
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.tv_search:
                if (event.getAction()==event.ACTION_DOWN){
                    expand();
                }
                break;
           /* case R.id.home_lv:
                if (event.getAction()==event.ACTION_DOWN){
                    reduce();
                }
                break;*/
        }
        return false;
    }
}
