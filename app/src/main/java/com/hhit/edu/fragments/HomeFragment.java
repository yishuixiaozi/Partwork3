package com.hhit.edu.fragments;

/*import android.app.Fragment;*/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hhit.edu.bean.HomePageBean;
import com.hhit.edu.partwork3.R;

import java.util.List;

import adapter.AbstractBaseAdapter;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 93681 on 2018/3/13.
 */

public class HomeFragment extends Fragment
    implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    static final int INTENT_REQUEST_CITY=1;//跳转到城市选择界面的requestcode

    static final int INTENT_REQUEST_SCAN=2;//跳转到二维码界面的requestcode这两个都不一定用得到

    TextView tv_cityname;
    ListView lv;
    //扫码控件
    ImageButton btn_scan;
    PtrFrameLayout refresh;//刷新控件

    String cityName;
    String cityId;

    String buttonmore="0";
    String pageflag="0";

    /*BannerView banner;//广告轮播对象
    HomeSecondView buttonView;//ListView中的8个按钮？
    */
    List<HomePageBean.DataEntity> data;//房子的各种信息的回馈实体

    AbstractBaseAdapter<HomePageBean.DataEntity> adapter;//<>这个是限制存储的数据的类型方式

    boolean idAddMore;//是否加载更多数据

    /**
     * 这个是无返回值的
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //三个方法
        getCityInfo();

        initData();

        getData();

       /* banner=new BannerView(getActivity());
        buttonView = new HomeSecondView(getActivity());*/
    }

    /**
     * 没写全
     */
    private void getCityInfo(){
        System.out.println("获取城市信息");
    }

    /**
     * 初始化数据
     */
    private void initData(){
        System.out.println("初始化数据");
    }

    private void getData(){
        System.out.println("联网获取城市数据");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shouye_fragment,container,false);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
