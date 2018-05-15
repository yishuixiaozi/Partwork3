package com.hhit.edu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhit.edu.partwork3.MypostActivity;
import com.hhit.edu.partwork3.R;
import com.j256.ormlite.stmt.query.In;

/**
 * Created by 93681 on 2018/5/15.
 */

public class Mine2Fragment extends Fragment implements View.OnClickListener{

    RelativeLayout rl_mypost;
    TextView txt_mypost;
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
            default:
                break;
        }
    }
}
