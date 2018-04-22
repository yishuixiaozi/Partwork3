package com.hhit.edu.partwork3;

import android.os.Build;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.fragments.DiscoverFragment;
import com.hhit.edu.fragments.MessageFragment;
import com.hhit.edu.fragments.MineFragment;
import com.hhit.edu.fragments.MyhomeFragment;

public class MainActivity extends AppCompatActivity {

    private String[] tabTexts={"首页","发现","消息","我的"};

    private int[] imgId={
            R.drawable.tab_home_selector,
            R.drawable.tab_discover_selector,
            R.drawable.tab_message_selector,
            R.drawable.tab_mine_selector,
    };

    private Class[] fragments = {
            MyhomeFragment.class, DiscoverFragment.class, MessageFragment.class, MineFragment.class
    };

    //定义一个控件的
    private FragmentTabHost tabHost;

    LayoutInflater inflater;//用来获得xml布局文件对象的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();//隐藏标题栏的作用
        setContentView(R.layout.activity_main);
        /*SharedUtils.saveFirstRun(this);用来保存第一次启动数据*/
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();//初始化控件的自定义方法
    }

    /**
     * 空间初始化
     */
    public void initView(){
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);//获得tahHost对象
        /**
         * 参数1：this,就是context上下文环境
         * 参数2：getSupportFragmentManager()  FragmentManager,
         * 参数3：R.id.fragmentLayout   容器，转载四个主的Fragment
         */
        tabHost.setup(this,getSupportFragmentManager(),R.id.fragmentLayout);

        inflater=LayoutInflater.from(this);//创建inflater这个对象

        for (int i=0;i<tabTexts.length;i++){

            TabHost.TabSpec tabItem=tabHost.newTabSpec(i+"");
            //给tabItem设置内容view
            tabItem.setIndicator(getTabItemView(i));
            //这里就相当于同时添加了标签和对应的内容了
            tabHost.addTab(tabItem,fragments[i],null);
            //这里是去边线
            tabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        }
    }

    /**
     * 这里就是依据传过来的参数形成一个菜单栏单项。比如，首页，发现，我的，
     * @param index
     * @return
     */
    private View getTabItemView(int index){
        View view=inflater.inflate(R.layout.tab_item_layout,null);
        //获取图形对象
        ImageView iv= (ImageView) view.findViewById(R.id.tab_img);
        TextView tv= (TextView) view.findViewById(R.id.tab_tv);
        iv.setImageResource(imgId[index]);
        tv.setText(tabTexts[index]);
        return view;
    }
    private long exitTime=0;

    /**
     * 按两次backspace退出应用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this,"再点击一次退出",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else{
                finish();
                //正常退出--0
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
