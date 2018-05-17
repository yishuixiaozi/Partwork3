package com.hhit.edu.partwork3;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hhit.edu.fragments.FindFragment;
import com.hhit.edu.fragments.MyhomeFragment;
import com.hhit.edu.fragments.NoreviewFragment;
import com.hhit.edu.fragments.NothroughFragment;
import com.hhit.edu.fragments.ThroughFragment;

import java.util.ArrayList;

import adapter.MyFragmentPagerAdapter;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager myviewpager;

    private ArrayList<Fragment> fragments;

    private Button btn_through;
    private Button btn_nothrough;
    private Button btn_noreview;

    private ImageView cursor;//下滑只是标签

    float cursorX=0;

    private int[] widthAegs;//宽度数组
    private Button[] btnArgs;//按钮数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initview();
    }
    //初始化布局
    public void initview(){
        myviewpager= (ViewPager) findViewById(R.id.myviewpager);
        btn_through= (Button) findViewById(R.id.btn_through);
        btn_nothrough= (Button) findViewById(R.id.btn_nothrough);
        btn_noreview= (Button) findViewById(R.id.btn_noreview);
        btnArgs=new Button[]{btn_through,btn_nothrough,btn_noreview};
        cursor= (ImageView) findViewById(R.id.cursor_btn);
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
        //将fragments数组传递过去作为参数形成adapter
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_through:
                myviewpager.setCurrentItem(0);
                cursorAnim(0);
                break;
            case R.id.btn_nothrough:
                myviewpager.setCurrentItem(1);
                cursorAnim(1);
                break;
            case R.id.btn_noreview:
                myviewpager.setCurrentItem(2);
                cursorAnim(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
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

    public void cursorAnim(int curItem){
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
