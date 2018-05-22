package com.hhit.edu.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hhit.edu.partwork3.R;

import java.util.ArrayList;

import adapter.MyFragmentPagerAdapter;

/**
 * Created by 93681 on 2018/5/22.
 */

public class ThroughresultFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private ViewPager myviewpager;
    private ArrayList<Fragment> fragments;
    private Button btn_through;
    private Button btn_nothrough;
    private Button btn_noreview;
    private ImageView cursor;//下滑只是标签
    float cursorX=0;
    private int[] widthAegs;//宽度数组
    private Button[] btnArgs;//按钮数组

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("------onCrate");
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("---onCreateView");
        return inflater.inflate(R.layout.activity_review, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("----onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    public void initview(View view){
        myviewpager= (ViewPager) view.findViewById(R.id.myviewpager);
        btn_through= (Button) view.findViewById(R.id.btn_through);
        btn_nothrough= (Button) view.findViewById(R.id.btn_nothrough);
        btn_noreview= (Button) view.findViewById(R.id.btn_noreview);
        btnArgs=new Button[]{btn_through,btn_nothrough,btn_noreview};
        cursor= (ImageView) view.findViewById(R.id.cursor_btn);
        cursor.setBackgroundColor(Color.RED);//下划线设置为红色
        myviewpager.setOnPageChangeListener(this);//说明这个方法已经过时了
        btn_through.setOnClickListener(this);
        btn_nothrough.setOnClickListener(this);
        btn_noreview.setOnClickListener(this);
        //将三个Fragment添加到对应的
        fragments=new ArrayList<>();
        fragments.add(new ThroughFragment());//这里是测试的内容
        fragments.add(new NothroughFragment());//这里也是测试的内容
        fragments.add(new NoreviewFragment());
        //这里是活动获取fragment作为参数传递
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),fragments);
        myviewpager.setAdapter(adapter);
        resetButtonColor();
        btn_through.setTextColor(Color.RED);//第一个设置为默认颜色状态
    }

    /**
     * 每次切换页面重新设置颜色
     */
    public void resetButtonColor(){
        btn_through.setBackgroundColor(Color.parseColor("#DCDCDC"));
        btn_nothrough.setBackgroundColor(Color.parseColor("#DCDCDC"));
        btn_noreview.setBackgroundColor(Color.parseColor("#DCDCDC"));
        btn_through.setTextColor(Color.BLACK);
        btn_nothrough.setTextColor(Color.BLACK);
        btn_noreview.setTextColor(Color.BLACK);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("fragments.size()="+fragments.size());
        System.out.println("onPageSelected---position="+position);
    //设置每个宽度数组的颜色
        if (widthAegs==null){
            widthAegs=new int[] {btn_through.getWidth(),btn_nothrough.getWidth(),btn_noreview.getWidth()};
        }
        //重置所有按钮颜色
        resetButtonColor();
        btnArgs[position].setTextColor(Color.RED);
        cursorAnim(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_through:
                myviewpager.setCurrentItem(0);
                break;
            case R.id.btn_nothrough:
                myviewpager.setCurrentItem(1);
                break;
            case R.id.btn_noreview:
                myviewpager.setCurrentItem(2);
                break;
        }
    }

    public void cursorAnim(int curItem){
        System.out.println("curItem----"+curItem);
        cursorX=0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)cursor.getLayoutParams();
        //减去边距*2，以对齐标题栏文字
        lp.width = widthAegs[curItem]-btnArgs[0].getPaddingLeft()*2;
        cursor.setLayoutParams(lp);
        //循环获取当前页之前的所有页面的宽度
        for(int i=0; i<curItem; i++){
            cursorX = cursorX + btnArgs[i].getWidth();
        }
        //再加上当前页面的左边距，即为指示器当前应处的位置
        cursor.setX(cursorX+btnArgs[curItem].getPaddingLeft());
    }
}
