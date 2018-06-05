package com.hhit.edu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.DropDownActivity;
import com.hhit.edu.partwork3.JobdetailsActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.uikit.DropDownMenu;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.PullToRefreshHeadView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.AbstractBaseAdapter;
import adapter.MenuListAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 93681 on 2018/4/24.
 */

public class FindFragment extends Fragment implements View.OnClickListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener{
    ListView lv;
    PtrFrameLayout refresh;
    String buttonmore="0";
    List<JobBean> jobdata;//实体数组内容
    AbstractBaseAdapter<JobBean> adapter;//<>这个是限制存储的数据的类型方式
    boolean isAddMore;//是否加载更多数据
    private int pagenum=0;
    //下面的这些都是初始化下拉组件的使用的
    private String hearders[] ={"兼职类型","结算时间","性别要求"};
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private MenuListAdapter mMenuAdapter1;
    private MenuListAdapter mMenuAdapter2;
    private MenuListAdapter mMenuAdapter3;
    private DropDownMenu mDropDownMenu;

    private EditText editText;
    private String citys[] = {"不限", "家教老师", "餐饮服务", "外卖服务", "快递服务", "校园代理", "发布传单", "校园兼职", "企业实习", "其他兼职"};
    private String ages[] = {"不限", "周末结算", "当天结算", "月末结算", "完工结算"};
    private String sexs[] = {"不限", "男", "女"};
    private String tabtext1="兼职类型";
    private String tabtext2="结算时间";
    private String tabtext3="性别要求";

    private String querytype="default";//这个就是默认查询的意思，kind是按类型查询，like是关键字查询
    private List<View> popupViews = new ArrayList<>();
    private JobBean jobBean=new JobBean();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobBean.setQuerytype(querytype);
        getData();
        initdata();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupview(view);
    }
    //初始化dropdownMenu组件的内容啊
    public void setupview(View view){
        mDropDownMenu= (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        lv= (ListView) view.findViewById(R.id.find_lv);
        editText= (EditText) view.findViewById(R.id.ed_search);
        listView1=new ListView(getActivity());
        listView2=new ListView(getActivity());
        listView3=new ListView(getActivity());
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER&&event.getAction()==event.ACTION_UP) {
                    System.out.println("你点击了搜索执行，输入值是"+editText.getText().toString());
                    jobBean.setQuerytype("like");
                    pagenum=0;//重新设置为0，搜索的时候
                    jobdata.clear();
                    jobBean.setQueryfield(editText.getText().toString());
                    getData();//关键字查询刷新数据
                }
                return false;
            }
        });
        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);
        listView3.setDividerHeight(0);

        //adapter赋值列表数据
        mMenuAdapter1=new MenuListAdapter(getActivity(), Arrays.asList(citys));
        mMenuAdapter2=new MenuListAdapter(getActivity(),Arrays.asList(ages));
        mMenuAdapter3=new MenuListAdapter(getActivity(),Arrays.asList(sexs));
        //列表连接adapter进行值显示
        listView1.setAdapter(mMenuAdapter1);
        listView2.setAdapter(mMenuAdapter2);
        listView3.setAdapter(mMenuAdapter3);
        //popuviews添加列表显示样式
        popupViews.clear();//先清空一下popupviews的存的值的内容，不然后期会导致数据不匹配的问题
        popupViews.add(listView1);
        popupViews.add(listView2);
        popupViews.add(listView3);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(citys[position]);
                System.out.println("----citys[position]"+citys[position]);
                tabtext1=citys[position];
                mDropDownMenu.closeMenu();
                getkinddata();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(ages[position]);
                tabtext2=ages[position];
                mDropDownMenu.closeMenu();
                getkinddata();
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(sexs[position]);
                tabtext3=sexs[position];
                mDropDownMenu.closeMenu();
                getkinddata();
            }
        });

        //这个地方是三步走的方式，添加头，选项，内容区
        TextView contentView=new TextView(getActivity());
        contentView.setText("这里是内容区域");
        contentView.setTextSize(20);
        contentView.setGravity(Gravity.CENTER);
        contentView.setVisibility(View.GONE);
        mDropDownMenu.setDropDownMenu(Arrays.asList(hearders),popupViews,contentView);

        setupRefresh(view);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(this);//设置监听方式
        lv.setOnItemClickListener(this);//设置Item点击监听
    }

    /**
     * 下拉列表点击
     */
    public void getkinddata(){
        System.out.println("tabtext1="+tabtext1+"tabtext2"+tabtext2+"tabtext3"+tabtext3);
        pagenum=0;//每次点击，pagenum重新赋值为0
        jobBean.setQuerytype("kind");
        jobBean.setJobtype(tabtext1);
        jobBean.setPayway(tabtext2);
        jobBean.setGender(tabtext3);
        jobdata.clear();
        getData();//刷新数据内容并且显示（多重筛选）
    }
    public void setupRefresh(View view){
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

    public void getData(){
        System.out.println("fragment-find-获取数据内容测试==============");
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobByType(jobBean,pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListResponse<JobBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        /*Toast.makeText(getActivity(),"数据加载中请稍后......",Toast.LENGTH_SHORT).show();*/
                        System.out.println("正在处理中-------");
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

                    }
                });
       /* request.getJobByPage(pagenum)
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
                });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    //完
    public void initdata(){
        System.out.println("------------------initdata()方法内容测试");
        jobdata=new ArrayList<>();
        adapter= new AbstractBaseAdapter<JobBean>(getActivity(), jobdata, R.layout.home_listview_mycontent) {
            @Override
            public void bindData(int position, ViewHolder holder) {
                JobBean jobBean=jobdata.get(position);//一个一个的赋值的我认为
                //查询的数据赋值开始
                ImageView iv= (ImageView) holder.findViewById(R.id.home_listview_img);
                Glide.with(FindFragment.this).load(jobBean.getJobimageuri()).into(iv); //图形赋值成功
                TextView tv_title= (TextView) holder.findViewById(R.id.home_listview_title);
                tv_title.setText(jobBean.getTitle());
                TextView tv_paymoney= (TextView) holder.findViewById(R.id.home_listview_paymoney);
                tv_paymoney.setText(jobBean.getPaymoney());
                TextView tv_payway= (TextView) holder.findViewById(R.id.home_listview_payway);
                tv_payway.setText(jobBean.getPayway());
                TextView tv_worktime= (TextView) holder.findViewById(R.id.home_listview_worktime);
                tv_worktime.setText(jobBean.getWorktime());
            }
        };
    }

    @Override
    public void onClick(View v) {

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
    /**
     * 列表单个元素的点击的事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("positon="+position);
        JobBean jobBean=jobdata.get(position);
        Intent intent=new Intent(getActivity(), JobdetailsActivity.class);
        intent.putExtra("id",jobBean.getId());
        intent.putExtra("userid",jobBean.getUserid());
        startActivity(intent);
    }
}
