package com.hhit.edu.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.LoginActivity;
import com.hhit.edu.partwork3.LoginnewActivity;
import com.hhit.edu.partwork3.MainActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 93681 on 2018/4/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{
    //
    private TextView loginTitle;
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


    private static final String ARG_SECTION_NUMBER = "section_number";
    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int sectionnumber=getArguments().getInt(ARG_SECTION_NUMBER);
        initview(view,sectionnumber);
           /* int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) view.findViewById(R.id.section_label);
            System.out.println("----"+getString(R.string.page,sectionNumber));
            textView.setText(getString(R.string.page, sectionNumber));*/
    }

    public void initview(View view,int sectionnumber){
        loginId = (EditText) view.findViewById(R.id.loginId);
        loginPassword = (EditText) view.findViewById(R.id.loginPassword);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        loginMissps = (Button) view.findViewById(R.id.loginMissps);
        loginMissps.setOnClickListener(this);
        loginNewUser = (Button) view.findViewById(R.id.loginNewUser);
        loginNewUser.setOnClickListener(this);
        loginChangePw = (Button) view.findViewById(R.id.loginChangePw);
        loginChangePw.setOnClickListener(this);
        //微信QQ的地三方登录
        im_loginqq= (ImageView) view.findViewById(R.id.im_loginqq);
        im_loginqq.setOnClickListener(this);
        im_loginweixin= (ImageView) view.findViewById(R.id.im_loginweixin);
        im_loginweixin.setOnClickListener(this);
        //免登录控件后期需要删掉
        nologin= (Button) view.findViewById(R.id.nologin);
        nologin.setOnClickListener(this);
        loginTitle= (TextView) view.findViewById(R.id.loginTitle);
        if (sectionnumber==2){//这里边可以把图片也换掉，这样两个就有点不一样了
            loginTitle.setText(getString(R.string.employee));
        }else{
            loginTitle.setText(getString(R.string.employer));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                System.out.println("你点击了登录这个选项");
                Toast.makeText(getActivity(),"事件点击确定",Toast.LENGTH_SHORT).show();
                System.out.println("参数设置----获取测试"+loginTitle.getText());
                login(loginTitle.getText().toString());
                break;
        }
    }

    /**
     * 数据库用户输入登录
     */
    public void login(final String usertype){
        System.out.println("登录操作--------");
        final String username=loginId.getText().toString();
        String password=loginPassword.getText().toString();
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.UserLogin(username,password,usertype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("----onSubscribe-----");
                    }
                    @Override
                    public void onNext(@NonNull EntityResponse<UserBean> userBeanEntityResponse) {
                        System.out.println("返回信息值处理"+userBeanEntityResponse.getMsg());
                        if ("success".equals(userBeanEntityResponse.getMsg())){
                            SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("nickname",username);
                            editor.putString("usertype",usertype);
                            editor.commit();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                            getActivity().finish();//关闭当前活动
                        }
                        else {
                            Toast.makeText(getActivity(),"用户名或密码错误！请重新登陆",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }
}
