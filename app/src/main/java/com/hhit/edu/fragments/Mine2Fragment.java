package com.hhit.edu.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhit.edu.partwork3.LoginforestActivity;
import com.hhit.edu.partwork3.LoginnewActivity;
import com.hhit.edu.partwork3.MypostActivity;
import com.hhit.edu.partwork3.R;
import com.j256.ormlite.stmt.query.In;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 93681 on 2018/5/15.
 */

public class Mine2Fragment extends Fragment implements View.OnClickListener{

    RelativeLayout rl_mypost;
    TextView txt_mypost;
    private TextView tv_name;
    private RelativeLayout extilogin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine2,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    /**
     * 初始化界面对象
     * @param view
     */
    public void initview(View view){
        txt_mypost= (TextView) view.findViewById(R.id.txt_mypost);
        txt_mypost.setOnClickListener(this);
        rl_mypost= (RelativeLayout) view.findViewById(R.id.rl_mypost);
        rl_mypost.setOnClickListener(this);
        tv_name= (TextView) view.findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);
        SharedPreferences preferences= getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
        String username=preferences.getString("nickname","未登录用户");//获取这个里边的值的内容
        tv_name.setText(username);
        extilogin= (RelativeLayout) view.findViewById(R.id.exitlogin);
        extilogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_mypost:
                //这里跳到所有的我的发布内容里边，显示我发布的内容
                Intent intent=new Intent(getActivity(), MypostActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mypost:
                Intent intent1=new Intent(getActivity(),MypostActivity.class);
                startActivity(intent1);
                break;
            case R.id.exitlogin:
                SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.remove("nickname");//这里是去除昵称：判断用户身份
                editor.remove("usertype");//这里是去除用户类型：判断进入的主界面
                editor.commit();
                startActivity(new Intent(getActivity(),LoginforestActivity.class));
                break;
            default:
                break;
        }
    }
}
