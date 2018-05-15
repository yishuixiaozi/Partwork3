package com.hhit.edu.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hhit.edu.partwork3.PostActivity;
import com.hhit.edu.partwork3.R;

/**
 * Created by 93681 on 2018/5/15.
 */

public class Myhome2Fragment extends Fragment implements View.OnClickListener{

    Button btn_post;
    Button btn_wait;
    Button btn_finsh;
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
        btn_post= (Button) view.findViewById(R.id.btn_post);
        btn_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                Intent intent_post=new Intent(getActivity(), PostActivity.class);
                startActivity(intent_post);
                break;
            default:
                break;
        }
    }
}
