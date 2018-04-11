package com.hhit.edu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.HomePageBean;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.JobdetailsActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.view.HomeSecondView;
import com.hhit.edu.view.PullToRefreshHeadView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import adapter.AbstractBaseAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 93681 on 2018/4/1.
 * 这里是我的首页内容进行初步调试测试
 */

public class MyhomeFragment extends Fragment implements View.OnClickListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener{
    TextView tv_cityName;//城市名
    ListView lv;//listview对象存放值的
    PtrFrameLayout refresh;//刷新控件
    String cityName;
    String cityId;
    String buttonmore="0";
    HomeSecondView SecondView;

    List<JobBean> jobdata;//实体数组内容
    AbstractBaseAdapter<JobBean> adapter;//<>这个是限制存储的数据的类型方式
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initdata();
        SecondView=new HomeSecondView(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupview(view);
    }

    /**
     * 初始化组件内容并且设置监听
     */
    public void setupview(View view){
        lv=(ListView) view.findViewById(R.id.home_lv);//列表组件需要先创建对象
        TextView btn1 = (TextView) SecondView.findViewById(R.id.tv_xinfang);
        TextView btn2 = (TextView) SecondView.findViewById(R.id.tv_ershoufang);
        setupRefreshView(view);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        lv.addHeaderView(SecondView);//加入八大组件内容
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        lv.setOnItemClickListener(this);//设置Item点击监听
        //restoreView();
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
                pagenum=0;//只要是刷新开始，pagenum就重置为0，不然的话，后面的内容太大，在此刷新的时候查询不到值的内容页面为空
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
     * 销毁首页
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    /**
     * 初始化数据内容
     */
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

    /**
     * 获取兼职信息简约信息
     * 测试rvjava和reftofit框架的组合使用，服务springMVC
     * http://192.168.137.1:8080/AndroidService/JobServlet/getAllJob
     */
    public void getData(){
        System.out.println("获取数据内容测试==============");
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080/AndroidService/")//http://192.168.0.101 192.168.137.1
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        final HomePageInterface request=retrofit.create(HomePageInterface.class);
        request.getJobByPage(pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListResponse<JobBean>>() {
                    @Override
                    public void accept(ListResponse<JobBean> jobBeanListResponse) throws Exception {
                        if (buttonmore.equals("0")) {//判断每次刷新的标志，每次刷新清空data列表
                            jobdata.clear();
                        }
                        jobdata.addAll(jobBeanListResponse.getItems());//为什么是添加？
                        adapter.notifyDataSetChanged();
                        refresh.refreshComplete();
                        buttonmore = "0";//这里就是让刷新的时候，能够让buttonmore为“0”
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_xinfang:{
                System.out.println("你点击了快递查询这个功能");
            }
            break;
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("一开始加载的时候显示内容");
        if (scrollState==0&&isAddMore){//一开始加载的时候显示内容，
            getData();
            buttonmore="1";
            System.out.println("-----------------pagenum="+pagenum);
            pagenum+=8;
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
        if (position>0){//将点击信息的ID传递过去显示详细内容
            JobBean jobBean=jobdata.get(position-1);
            Intent intent=new Intent(getActivity(), JobdetailsActivity.class);
            intent.putExtra("id",jobBean.getId());
            intent.putExtra("userid",jobBean.getUserid());
            startActivity(intent);
        }
    }
}
