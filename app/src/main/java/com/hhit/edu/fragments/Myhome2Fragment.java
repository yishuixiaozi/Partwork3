package com.hhit.edu.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hhit.edu.partwork3.PostActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.partwork3.ReviewActivity;
import com.hhit.edu.partwork3.ReviewokActivity;
import com.hhit.edu.partwork3.ReviewsorryActivity;
import com.hhit.edu.partwork3.ReviewwaitActivity;
import com.hhit.edu.partwork3.TestSearchActivity;
import com.j256.ormlite.field.types.IntegerObjectType;
import com.j256.ormlite.stmt.query.In;

/**
 * Created by 93681 on 2018/5/15.
 */

public class Myhome2Fragment extends Fragment implements View.OnClickListener{
    LinearLayout ll_post;
    LinearLayout ll_ok;
    LinearLayout ll_wait;
    LinearLayout ll_sorry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home2_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    /**
     * 初始化部分组件
     * @param view
     */
    public void initview(View view){
        ll_post= (LinearLayout) view.findViewById(R.id.ll_post);
        ll_ok= (LinearLayout) view.findViewById(R.id.ll_ok);
        ll_wait= (LinearLayout) view.findViewById(R.id.ll_wait);
        ll_sorry= (LinearLayout) view.findViewById(R.id.ll_sorry);
        ll_post.setOnClickListener(this);
        ll_ok.setOnClickListener(this);
        ll_wait.setOnClickListener(this);
        ll_sorry.setOnClickListener(this);
        ll_ok.getBackground().setAlpha(100);
        //这个地方让其他的几面也透明度变化，因为共同使用#FFF，所以一样变化
        //ll_ok.getBackground()..mutate().setAlpha(alpha); 就只会变指定背景透明度
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_post:
                Intent intent_post=new Intent(getActivity(), PostActivity.class);
                startActivity(intent_post);
                break;
            case R.id.ll_ok:
                Intent intent=new Intent(getActivity(), ReviewokActivity.class);
                startActivity(intent);
                //所有的集合体，留作参考使用
                /*Intent intent_through=new Intent(getActivity(), ReviewActivity.class);
                startActivity(intent_through);*/
                break;
            case R.id.ll_wait:
                Intent intent1=new Intent(getActivity(), ReviewwaitActivity.class);
                startActivity(intent1);
                //搜索框使用内容
               /* Intent intent=new Intent(getActivity(), TestSearchActivity.class);
                startActivity(intent);*/
                break;
            case R.id.ll_sorry:
                Intent intent2=new Intent(getActivity(), ReviewsorryActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
