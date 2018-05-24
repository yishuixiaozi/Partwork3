package com.hhit.edu.fragments;
import android.content.Intent;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.JobneedBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.ExpressActivity;
import com.hhit.edu.partwork3.JobdetailsActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.HomeSecondView;
import com.hhit.edu.view.PullToRefreshHeadView;
import com.hhit.edu.view.ToorbarView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;
import adapter.AbstractBaseAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static android.content.Context.MODE_PRIVATE;
/**
 * Created by 93681 on 2018/4/1.
 * 这里是我的首页内容进行初步调试测试
 */
public class MyhomeFragment extends Fragment implements View.OnClickListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener,View.OnTouchListener{
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//布局内容
    String buttonmore="0";
    List<JobBean> jobdata;//实体数组内容
    AbstractBaseAdapter<JobBean> adapter;//<>这个是限制存储的数据的类型方式
    boolean isAddMore;//是否加载更多数据
    boolean iskeyData=false;
    private int pagenum=0;
    String myuserid;
    boolean isExpand = false;
    EditText tvSearch;
    LinearLayout mSearchLayout;
    Toolbar toolbar;
    private TransitionSet mSet;
    ToorbarView toorbarView;
    String queryfield;
    HomeSecondView homeSecondView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名userid---------测试"+myuserid);*/
        getData();
        initdata();
        toorbarView=new ToorbarView(getActivity());
        homeSecondView=new HomeSecondView(getActivity());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container,false);
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
        System.out.println("获取数据内容测试==============");
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobByPage(pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //Toast.makeText(getActivity(),"数据加载中请稍后......",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(@NonNull ListResponse<JobBean> jobBeanListResponse) {
                        if (buttonmore.equals("0")) {//判断每次刷新的标志，每次刷新清空data列表
                            jobdata.clear();
                        }
                        jobdata.addAll(jobBeanListResponse.getItems());//为什么是添加？
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity(),"服务器连接异常，请检查！",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                        //Toast.makeText(getActivity(),"数据加载成功",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 这是首页的
     * @param queryfield
     */
    public void keygetData(String queryfield){
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobhomelike(queryfield,pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ListResponse<JobBean> jobBeanListResponse) {
                        if(buttonmore.equals("0")){
                            jobdata.clear();
                        }
                        jobdata.addAll(jobBeanListResponse.getItems());
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        iskeyData=true;
                        buttonmore="0";
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("--检查服务器和网络IP的问题");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initdata(){
        System.out.println("------------------initdata()方法内容测试");
        jobdata=new ArrayList<>();
        adapter=new AbstractBaseAdapter<JobBean>(getActivity(),jobdata, R.layout.home_listview_mycontent) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                JobBean jobBean=jobdata.get(position);//一个一个的赋值的我认为
                //查询的数据赋值开始
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(MyhomeFragment.this).load(jobBean.getJobimageuri()).into(iv); //图形赋值成功
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

    public void setupview(View view){
        TextView tv_express= (TextView) homeSecondView.findViewById(R.id.tv_express);
        tv_express.setOnClickListener(this);
        setupRefreshView(view);
        tvSearch= (EditText) view.findViewById(R.id.tv_search);
        mSearchLayout= (LinearLayout) view.findViewById(R.id.ll_search);
        toolbar= (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.getBackground().mutate().setAlpha(0);
        tvSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER&&event.getAction()==event.ACTION_UP) {
                    System.out.println("你点击了搜索执行，输入值是"+tvSearch.getText().toString());
                    queryfield=tvSearch.getText().toString();
                    pagenum=0;
                    keygetData(queryfield);
                }
                return false;
            }
        });
        lv=(ListView) view.findViewById(R.id.home_lv);
        lv.addHeaderView(toorbarView);
        lv.addHeaderView(homeSecondView);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        tvSearch.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);
        tvSearch.setOnTouchListener(this);
        //refresh.setOnTouchListener(this);
        lv.setOnTouchListener(this);
        lv.setOnItemClickListener(this);//设置Item点击监听
    }
    public void setupRefreshView(View view){
        refresh= (PtrFrameLayout) view.findViewById(R.id.refresh);//获得可刷新对象
        PullToRefreshHeadView pullHead=new PullToRefreshHeadView(getContext());//这句话可以改变刷新头控件
        //PullToRefreshView pullToRefreshView=new PullToRefreshView(getContext());
        //添加刷新tou
        refresh.setHeaderView(pullHead);
        refresh.addPtrUIHandler(pullHead);
      /* refresh.setHeaderView(pullToRefreshView);
        refresh.addPtrUIHandler(pullToRefreshView);*/
         refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("-----------------onRefreshLayout---"+buttonmore);
                pagenum=0;
                iskeyData=false;//在设置为false，刷新后下滑获取原始数据
                getData();//只要刷新就让他执行原始数据获取
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
            case R.id.tv_express:
                System.out.println("快递测试");
                startActivity(new Intent(getActivity(), ExpressActivity.class));
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("--");
        if (scrollState==0&&isAddMore){//一开始加载的时候显示内容，
            if (iskeyData){
                System.out.println("是关键字查询");
                pagenum+=8;
                keygetData(queryfield);
                buttonmore="1";
            }
            else {
                pagenum+=8;
                getData();
                buttonmore="1";
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int toplong=Math.abs(toorbarView.getTop());
        changeToolbarAlpha(toplong);
        changeViewhead(toplong);
        if (firstVisibleItem + visibleItemCount == totalItemCount) {//就是说下拉看到最后一条了，设置isAddMore的属性为true，改变新的参数查询内容
            isAddMore = true;
        } else {
            isAddMore=false;
        }
    }

    private void changeViewhead(int ScrollY){
        if (ScrollY >=toorbarView.getHeight() - toolbar.getHeight()  && !isExpand) {
            expand();
            isExpand = true;
        }
        //滚动距离<=0时 即滚动到顶部时  且当前伸展状态 进行收缩操作
        else if (ScrollY<=10&& isExpand) {
            reduce();
            isExpand = false;
        }
    }
    private void changeToolbarAlpha(int scrollY) {
        //快速下拉会引起瞬间scrollY<0
        if(scrollY<0){
            toolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前透明度比率
        float radio= Math.min(1,scrollY/(toorbarView.getHeight()-toolbar.getHeight()*1f));
        //设置透明度
        toolbar.getBackground().mutate().setAlpha( (int)(radio * 0xFF));
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("positon="+position);
        if (position>0){//将点击信息的ID传递过去显示详细内容
            JobBean jobBean=jobdata.get(position-1);
            Intent intent=new Intent(getActivity(), JobdetailsActivity.class);
            intent.putExtra("id",jobBean.getId());
            intent.putExtra("userid",jobBean.getUserid());
            startActivity(intent);
        }
    }


    private void expand() {
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void reduce() {
        //设置收缩状态时的布局
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
        }
        return false;
    }
}