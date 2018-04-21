package com.hhit.edu.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hhit.edu.partwork3.LoginnewActivity;
import com.hhit.edu.partwork3.R;

/**
 * Created by 93681 on 2018/4/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

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
        initview(view);
           /* int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) view.findViewById(R.id.section_label);
            System.out.println("----"+getString(R.string.page,sectionNumber));
            textView.setText(getString(R.string.page, sectionNumber));*/
    }

    public void initview(View view){
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                System.out.println("你点击了登录这个选项");
                Toast.makeText(getActivity(),"事件点击确定",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
