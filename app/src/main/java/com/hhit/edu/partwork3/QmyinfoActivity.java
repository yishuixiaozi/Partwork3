package com.hhit.edu.partwork3;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QmyinfoActivity extends AppCompatActivity implements View.OnClickListener{
    @InjectView(R.id.im_back)
    ImageView im_back;
    @InjectView(R.id.tv_username)
    TextView tv_usernmae;
    @InjectView(R.id.edt_nickname)
    EditText edt_nickname;
    @InjectView(R.id.edt_year)
    EditText edt_year;
    @InjectView(R.id.edt_city)
    EditText edt_city;
    @InjectView(R.id.edt_gender)
    EditText edt_gender;
    @InjectView(R.id.edt_phonenum)
    EditText edt_phonum;
    @InjectView(R.id.edt_sumary)
    EditText edt_sumary;
    @InjectView(R.id.tv_xiugai)
    TextView tv_xiugai;
    boolean isEnabled=false;
    SharedPreferences sharedPreferences;
    String userid;
    UserBean userBean=new UserBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmyinfo);
        sharedPreferences=this.getSharedPreferences("mydata",MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","default");
        ButterKnife.inject(this);
        initView();
        getUserData();
        chagestate();//首先设置为不可编辑状态
    }

    public void initView(){
        im_back.setOnClickListener(this);
        tv_xiugai.setOnClickListener(this);
    }

    public void getUserData(){
        HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getUserByUserid(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull EntityResponse<UserBean> userBeanEntityResponse) {
                        userBean=userBeanEntityResponse.getObject();
                        initData(userBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("---获取用户失败了");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initData(UserBean userBean){
        //赋值操作
        tv_usernmae.setText(userBean.getUsername());
        edt_nickname.setText(userBean.getNickname());
        edt_year.setText(userBean.getYear());
        edt_city.setText(userBean.getCity());
        edt_phonum.setText(userBean.getPhonenum());
        edt_gender.setText(userBean.getGender());
        edt_sumary.setText(userBean.getSumary());
    }
    public void chagestate(){
        edt_nickname.setEnabled(isEnabled);
        edt_city.setEnabled(isEnabled);
        edt_year.setEnabled(isEnabled);
        edt_gender.setEnabled(isEnabled);
        edt_phonum.setEnabled(isEnabled);
        edt_sumary.setEnabled(isEnabled);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_xiugai://点击修改
                if (tv_xiugai.getText().toString().equals("点击编辑")){
                    tv_xiugai.setText("保存更改");
                    tv_xiugai.setBackgroundColor(Color.RED);
                    isEnabled=true;
                    chagestate();//设置可编辑状态
                }else {//点击保存
                    new SweetAlertDialog(QmyinfoActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定保存修改?")
                            .setContentText("个人信息更有魅力！")
                            .setCancelText("不，取消")
                            .setConfirmText("是，保存")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    // reuse previous dialog instance, keep widget user state, reset them if you need
                                    sDialog.setTitleText("取消")
                                            .setContentText("它依然在你身边")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    updateQuser();//确认保存修改
                                    sDialog.setTitleText("已更改")
                                            .setContentText("个人信息更具吸引力！")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }
                            })
                            .show();
                    tv_xiugai.setText("编辑");
                    tv_xiugai.setBackgroundColor(getColor(R.color.blue));
                    isEnabled=false;
                    chagestate();//设置为不可编辑状态
                }
                break;
        }
    }

    public void updateQuser(){
        userBean.setUserid(userid);
        userBean.setNickname(edt_nickname.getText().toString());
        userBean.setYear(edt_year.getText().toString());
        userBean.setCity(edt_city.getText().toString());
        userBean.setGender(edt_city.getText().toString());
        userBean.setPhonenum(edt_phonum.getText().toString());
        userBean.setSumary(edt_sumary.getText().toString());
        HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.updateQuserinfo(userBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("更新数据成功了");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("更新数据出错了");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
