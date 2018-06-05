package com.hhit.edu.partwork3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.hhit.edu.view.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static butterknife.ButterKnife.Finder.DIALOG;

public class StudentinfoActivity extends AppCompatActivity implements View.OnClickListener{
    @InjectView(R.id.circle_img)
    ImageView im_view;
    @InjectView(R.id.txt_username)
    TextView txt_username;
    @InjectView(R.id.txt_phonum)
    TextView txt_phonenum;
    @InjectView(R.id.txt_nickname)
    TextView txt_nickname;
    @InjectView(R.id.txt_year)
    TextView txt_year;
    @InjectView(R.id.txt_gender)
    TextView txt_gender;
    @InjectView(R.id.txt_sumary)
    TextView sumary;
    @InjectView(R.id.tv_goutong)
    TextView txt_goutong;
    UserBean userBean=new UserBean();
    String userid;
    private final static int DIALOG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);
        ButterKnife.inject(this);
        userid=getIntent().getStringExtra("userid");
        System.out.println("-------userid="+userid);
        getData();
        initview();
    }
    public void initview(){
        txt_goutong.setOnClickListener(this);
    }

    //获取用户信息
    public void getData(){
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getQuserinfo(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull EntityResponse<UserBean> userBeanEntityResponse) {
                        userBean=userBeanEntityResponse.getObject();
                        initdata(userBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("获取信息失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        //处理电话业务内容
        switch (v.getId()){
            case R.id.tv_goutong:
                showDialog(DIALOG);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) {
            case DIALOG:
                AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
                //设置对话框的图标
                builder.setIcon(R.drawable.collect_icon);
                //设置对话框的标题
                builder.setTitle("列表对话框");
                //添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
                builder.setItems(R.array.goutong, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        String hoddy=getResources().getStringArray(R.array.goutong)[which];
                        System.out.println("------hoddy"+hoddy);
                        goutong(hoddy);
                    }
                });
                //创建一个列表对话框
                dialog=builder.create();
                break;
        }
        return dialog;
    }

    public void goutong(String hoddy){
        if (hoddy.equals("电话联系")){
            Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+userBean.getPhonenum()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (hoddy.equals("短信联系")){
            Intent intent=new Intent(this,SendmessageActivity.class);
            intent.putExtra("phonenumber",userBean.getPhonenum());
            startActivity(intent);
        }else {
            System.out.println("没有这个内容");
        }
    }
    public void initdata(UserBean userBean){
        Glide.with(StudentinfoActivity.this).load(userBean.getPhotouri()).into(im_view);
        txt_username.setText(userBean.getUsername());
        txt_phonenum.setText(userBean.getPhonenum());
        txt_nickname.setText(userBean.getNickname());
        txt_year.setText(userBean.getYear());
        txt_gender.setText(userBean.getGender());
    }
}
