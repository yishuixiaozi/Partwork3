package com.hhit.edu.partwork3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.hhit.edu.utils.MyBaseUIlistener;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.Locale;
public class LoginnewActivity extends AppCompatActivity implements View.OnClickListener{
/*    SectionsPagerAdapter sectionsPagerAdapter=new SectionsPagerAdapter(FragmentManager );*/
    SectionsPagerAdapter sectionsPagerAdapter;
    private ImageView login_qq;
    private MyBaseUIlistener myBaseUIlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        String username=preferences.getString("nickname","default");
        if(username.equals("default")){
            setContentView(R.layout.activity_loginnew);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().hide();
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(sectionsPagerAdapter);
            viewPager.setCurrentItem(1);
            LivingTabsLayout tabs = (LivingTabsLayout) findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            myBaseUIlistener=new MyBaseUIlistener(this);
        }else {
            System.out.println("已经存值--------username----------------"+username);
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }

    /**
     * 测试内容
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,myBaseUIlistener);
        }
       // System.out.println("activity-------里边的内容="+data.getData().toString());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_loginqq:
                System.out.println("activity中活动的点击qq");
                break;
        }
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
            switch (position) {
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
