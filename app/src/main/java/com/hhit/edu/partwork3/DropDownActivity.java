package com.hhit.edu.partwork3;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhit.edu.uikit.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.MenuListAdapter;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class DropDownActivity extends AppCompatActivity{
    private String hearders[] ={"城市","年龄","性别"};
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private ListView home_lv;

    private MenuListAdapter mMenuAdapter1;
    private MenuListAdapter mMenuAdapter2;
    private MenuListAdapter mMenuAdapter3;

    private DropDownMenu mDropDownMenu;
    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};

    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};

    private String sexs[] = {"不限", "男", "女"};

    private List<View> popupViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down);

        initView();
    }

    /**
     * 初始化组件内容
     */
    private void initView(){
        mDropDownMenu= (DropDownMenu) findViewById(R.id.dropDownMenu);//获取页面对象
        listView1=new ListView(DropDownActivity.this);
        listView2=new ListView(DropDownActivity.this);
        listView3=new ListView(DropDownActivity.this);
        home_lv=new ListView(DropDownActivity.this);
        //设置分割线高度
        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);
        listView3.setDividerHeight(0);
        //adapter赋值列表数据
        mMenuAdapter1=new MenuListAdapter(DropDownActivity.this, Arrays.asList(citys));
        mMenuAdapter2=new MenuListAdapter(DropDownActivity.this,Arrays.asList(ages));
        mMenuAdapter3=new MenuListAdapter(DropDownActivity.this,Arrays.asList(sexs));
        //列表连接adapter进行值显示
        listView1.setAdapter(mMenuAdapter1);
        listView2.setAdapter(mMenuAdapter2);
        listView3.setAdapter(mMenuAdapter3);
        //popuviews添加列表显示样式
        popupViews.add(listView1);
        popupViews.add(listView2);
        popupViews.add(listView3);

        //列表1的点击事件
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(citys[position]);
                System.out.println("----citys[position]"+citys[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(ages[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(sexs[position]);
                mDropDownMenu.closeMenu();
            }
        });
         TextView contentView=new TextView(this);
        contentView.setText("这里是内容区域");
        contentView.setTextSize(20);
        contentView.setGravity(Gravity.CENTER);
        contentView.setVisibility(View.GONE);
        //这个地方是三步走的方式，添加头，选项，内容区
        mDropDownMenu.setDropDownMenu(Arrays.asList(hearders),popupViews,contentView);

    }

}
