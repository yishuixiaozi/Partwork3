package com.hhit.edu.partwork3;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.hhit.edu.fragments.LoginFragment;
import com.hhit.edu.utils.LivingTabsLayout;
import java.util.Locale;
public class LoginnewActivity extends AppCompatActivity implements View.OnClickListener{
    //登录界面的必要参数
    private EditText loginId;
    private EditText loginPassword;
    private Button loginBtn;
    private Button loginMissps;
    private Button loginNewUser;
    private Button loginChangePw;
    //这个用于测试内容
    private ImageView im_loginqq;
    private ImageView im_loginweixin;
    //免登录使用
    private Button nologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginnew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1);
        LivingTabsLayout tabs = (LivingTabsLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter implements LivingTabsLayout.DrawableResIconAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LoginFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {//字迹获取
                case 0:
                    return getString(R.string.popular).toUpperCase(l);
                case 1:
                    return getString(R.string.home).toUpperCase(l);
               /* case 2:
                    return getString(R.string.account).toUpperCase(l);*/
            }
            return null;
        }

        @Override
        public int getIcon(int position) {//图形获取
            switch (position) {
                case 1:
                    return R.drawable.ic_home;
                case 0:

                    return R.drawable.ic_fire;
              /*  case 2:
                    return R.drawable.ic_account;*/
            }
            return -1;
        }
    }

   /* public static class PlaceholderFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_login, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
           *//* int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) view.findViewById(R.id.section_label);
            System.out.println("----"+getString(R.string.page,sectionNumber));
            textView.setText(getString(R.string.page, sectionNumber));*//*
        }*/

        //初始化组件
        /*  public void initview(View view){
          loginId = (EditText) view.findViewById(R.id.loginId);
            loginPassword = (EditText) findViewById(R.id.loginPassword);
            loginBtn = (Button) findViewById(loginBtn);
            loginBtn.setOnClickListener(this);
            loginMissps = (Button) findViewById(R.id.loginMissps);
            loginMissps.setOnClickListener(this);
            loginNewUser = (Button) findViewById(R.id.loginNewUser);
            loginNewUser.setOnClickListener(this);
            loginChangePw = (Button) findViewById(R.id.loginChangePw);
            loginChangePw.setOnClickListener(this);
            //微信QQ的地三方登录
            im_loginqq= (ImageView) findViewById(R.id.im_loginqq);
            im_loginqq.setOnClickListener(this);
            im_loginweixin= (ImageView) findViewById(R.id.im_loginweixin);
            im_loginweixin.setOnClickListener(this);
            //免登录控件后期需要删掉
            nologin= (Button) findViewById(R.id.nologin);
            nologin.setOnClickListener(this);
        }*/
}
