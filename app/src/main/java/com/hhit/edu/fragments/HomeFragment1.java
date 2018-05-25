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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhit.edu.bean.HomePageBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.ExpressActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.SharedUtils;
import com.hhit.edu.view.HomeSecondView;
import com.hhit.edu.view.PullToRefreshHeadView;
import java.util.ArrayList;
import java.util.List;
import adapter.AbstractBaseAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
/**
 * Created by 93681 on 2018/3/16.
 */
public class HomeFragment1 extends Fragment
        implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{
    TextView tv_cityName;
    ListView lv;
    ImageButton btn_scan;
    PtrFrameLayout refresh;//刷新控件
    String cityName;
    String cityId;
    String buttonmore="0";
    String pageflag="0";
    /*BannerView banner;*///广告轮播对象
    HomeSecondView buttonView;//ListView中的8个按钮？
    List<HomePageBean.DataEntity> data;//房子的各种信息的回馈实体
    AbstractBaseAdapter<HomePageBean.DataEntity> adapter;//<>这个是限制存储的数据的类型方式
    boolean isAddMore;//是否加载更多数据
    /**
     * 这个是无返回值的
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//1
        super.onCreate(savedInstanceState);
        System.out.println("-------------------------onCreate()");
        getCityInfo();//设置城市：北京  ID
        initData();//这里就是给listView里边的Item单项进行值得给予，形成最终的图形内容
        getData();
        buttonView = new HomeSecondView(getActivity());//8个导航页面的内容
    }
    /**
     * 没写全
     */
    private void getCityInfo(){
        String[] cityInfo= SharedUtils.getCityInfo(getContext());
        cityName=cityInfo[0];//北京
        cityId=cityInfo[1];//id号
    }
    /**
     * 初始化数据
     */
    private void initData(){//2
        System.out.println("-----------------initData");
        data=new ArrayList<>();
        System.out.println("data.size"+data.size());
        adapter=new AbstractBaseAdapter<HomePageBean.DataEntity>(getActivity(), data, R.layout.home_listview_content, R.layout.home_listview_content2) {
            @Override
            public void bindData(int position, ViewHolder holder) {//holder应该就是指上面的两个Item单项内容了
                System.out.println("2data.size"+data.size());
                HomePageBean.DataEntity entity=data.get(position);
                //得到接口返回的数据类型，就可确定布局类型，依据不同布局加载不同数据
                int type=Integer.valueOf(data.get(position).getType());
                if (type==0){
                    ImageView iv=(ImageView) holder.findViewById(R.id.home_listview_img);
                    Glide.with(HomeFragment1.this).load(entity.getGroupthumbnail()).into(iv);
                    TextView tv_title=(TextView) holder.findViewById(R.id.home_listview_title);
                    tv_title.setText(entity.getTitle());
                    TextView tv_content = (TextView) holder.findViewById(R.id.home_listview_summary);
                    tv_content.setText(entity.getSummary());
                }
                else {
                    ImageView iv = (ImageView) holder.findViewById(R.id.home_listview_img);
                    Glide.with(HomeFragment1.this).load(entity.getGroupthumbnail()).into(iv);
                    TextView tv_title = (TextView) holder.findViewById(R.id.home_listview_title);
                    tv_title.setText(entity.getTitle());
                }
            }
            /**
             * 感觉没用到
             * @param position 每条实体数据的下表
             * @return type类型，判断给哪个数据填充内容
             */
            @Override
            public int getItemViewType(int position) {
                HomePageBean.DataEntity entity = data.get(position);
                String type = entity.getType();
                return Integer.valueOf(type);
            }
        };
    }
    /**
     * 获取该城市的数据
     */
    private void getData(){//3
        System.out.println("------------------------------getData");
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiManager.BASE_URL)//设置baseURL http://ikft.house.qq.com/  会进行url重新组装的
                .addConverterFactory(ScalarsConverterFactory.create())//添加Gson转换器
                .build();
        //http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19
        //&devid=866500021200250&appname=QQHouse&mod=appkft&reqnum=20&act=newslist&channel=71&pageflag="0"&buttonmore=...
        HomePageInterface homePage = retrofit.create(HomePageInterface.class);

        /**
         * 参数：pageflag;buttonmore;cityid
         * 1)进入时：reqnum=10,pageflag=0,buttonmore=0;
         * 2)点击查看更多时：reqnum=20,pageflag=0,buttonmore=1;
         * 3)刷新时：reqnum=20,pageflag=1,buttonmore=1;
         */
        Call<String> call = homePage.getListContent("0", buttonmore, cityId);//这里均使用异步，同步不能使用会阻塞
        call.enqueue(new Callback<String>() {//异步请求
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body();
                //用Gson把String解析成HomePageBean
                Gson gson = new Gson();
                HomePageBean bean = gson.fromJson(value, new TypeToken<HomePageBean>() {
                }.getType());
                if (buttonmore.equals("0")) {
                    data.clear();
                }
                data.addAll(bean.getData());
                adapter.notifyDataSetChanged();
                refresh.refreshComplete();
                buttonmore = "0";
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                refresh.refreshComplete();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {//4
        System.out.println("-----------------------onCreateView");
        return inflater.inflate(R.layout.home_fragment,container,false);
    }
    /**
     * 视图创建完成后执行
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {//5
        System.out.println("------------------------------onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        setupView(view);//感觉像是home_fragment
    }
    private void setupView(View view){//6
        System.out.println("-------------------------------setupView");
        lv = (ListView) view.findViewById(R.id.home_lv);
        TextView btn1 = (TextView) buttonView.findViewById(R.id.tv_express);
        TextView btn4 = (TextView) buttonView.findViewById(R.id.tv_zixun);
        setupRefreshView(view);//初始化Refresh控件
        //我的上一个是因为没有设置监听对象所以点击无效
        btn1.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn_scan.setOnClickListener(this);//二维码的监听事件
        lv.addHeaderView(buttonView);//listView里边添加了八图标
        tv_cityName.setText(cityName);
        //设置tv_cityname的点击监听事件
        tv_cityName.setOnClickListener(this);
        //lv关联Adapter
        lv.setAdapter(adapter);
        //少了监听导致了无法刷新吗
        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(this);//Item选择的内容
        restoreView();
    }
    private void setupRefreshView(View view){//7
        System.out.println("----------------------setupRefreshView");
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
     *城市设置名字的，为了加载相应的地方的数据类容
     */
    private void restoreView(){
        System.out.println("restoreView ----从setupView 过来的");
        if (cityName != null) {
            tv_cityName.setText(cityName);
        }
    }
    /**
     * 销毁首页
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    /**
     * 这里主要处理的是八大组件控制（暂时无用）
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_express: {//快递查询功能
                startActivity(new Intent(getActivity(), ExpressActivity.class));
            }
            break;
            case R.id.tv_zixun: {
            }
            break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("---------------------onScrollStateChanged");
        if (scrollState == 0 && isAddMore) {
            getData();
            buttonmore = "1";
        }
    }
    /**
     * 设置isAddMore的值，判断是否要加载更多的内容显示（完）
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {//8
        System.out.println("onScroll----");
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            isAddMore = true;
        } else {
            isAddMore = false;
        }
    }
    /**
     * 点击某一个Item进入详细的兼职信息显示界面（未完，暂时无用）
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position>=lv.getHeaderViewsCount()) {
            String webId = data.get(position - lv.getHeaderViewsCount()).getId();
            //用WebView显示信息界面
//        Intent intent = new Intent(getActivity(), WebViewActivity.class);
//        intent.putExtra(IntentUtils.KEY_ITEMID,webId);
//        startActivity(intent);
            //自定义布局显示详细的兼职信息内容，需要自己去编写这个界面了
            /*Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
            intent.putExtra(IntentUtils.KEY_NEWSID, webId);
            startActivity(intent);*/
        }
    }
}
