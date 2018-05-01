package com.hhit.edu.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.partwork3.CollectionActivity;
import com.hhit.edu.partwork3.EditPhotoActivity;
import com.hhit.edu.partwork3.LoginActivity;
import com.hhit.edu.partwork3.LoginnewActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.partwork3.SignupActivity;
import com.hhit.edu.view.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 93681 on 2018/3/13.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private CircleImageView cricle_image;
    private ImageView editphotoimg;
    private TextView tv_name;
    private RelativeLayout extilogin;
    private RelativeLayout mycollection;
    private RelativeLayout mysignup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);//这个就是初始化的内容了
    }
    /**
     *
     * @param view
     */
    private void initView(View view){
        //获取点击，让它点击后，跳入到登录界面
        cricle_image= (CircleImageView) view.findViewById(R.id.circle_img);
        cricle_image.setOnClickListener(this);
        editphotoimg= (ImageView) view.findViewById(R.id.edit_img);
        editphotoimg.setOnClickListener(this);
        tv_name= (TextView) view.findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);
        mycollection= (RelativeLayout) view.findViewById(R.id.mycollection);
        mycollection.setOnClickListener(this);
        mysignup= (RelativeLayout) view.findViewById(R.id.mysignup);
        mysignup.setOnClickListener(this);
        SharedPreferences preferences= getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
        String username=preferences.getString("nickname","未登录用户");//获取这个里边的值的内容
        tv_name.setText(username);

        extilogin= (RelativeLayout) view.findViewById(R.id.exitlogin);
        extilogin.setOnClickListener(this);
    }
    /**
     * 对所有的控件的点击事件进行一个整体的汇总点击
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击Fragment_mine中的头像进入登录页面
            case R.id.circle_img:
                Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(login_intent);
                break;
            //点击Fragment_mine页面中的向右的箭头，进入图片上传修改该界面
            case R.id.tv_name:
                Intent editph_intent = new Intent(getActivity(), EditPhotoActivity.class);
                startActivity(editph_intent);
                break;
            case R.id.edit_img:
                Intent editph_intent1 = new Intent(getActivity(), EditPhotoActivity.class);
                startActivity(editph_intent1);
                break;
            case R.id.exitlogin:
                SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.remove("nickname");
                editor.commit();
                startActivity(new Intent(getActivity(),LoginnewActivity.class));
                break;
            case R.id.mycollection://收藏
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            case R.id.mysignup://报名
                startActivity(new Intent(getActivity(), SignupActivity.class));
                break;
            default:
                break;
        }
    }
}
