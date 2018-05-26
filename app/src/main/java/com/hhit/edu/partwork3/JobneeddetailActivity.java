package com.hhit.edu.partwork3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobneedBean;
import com.hhit.edu.my_interface.JobneedPageinterface;
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

public class JobneeddetailActivity extends AppCompatActivity implements View.OnClickListener{
    @InjectView(R.id.jobtitle)
    EditText txt_jobneedtitle;
    @InjectView(R.id.txt_jobtype)
    EditText txt_jobneedtype;
    @InjectView(R.id.txt_bftime)
    EditText txt_jobneedbftime;
    @InjectView(R.id.txt_worktime)
    EditText txt_jobneedworktime;
    @InjectView(R.id.txt_jobneedcontent)
    EditText txt_jobneedcontent;
    @InjectView(R.id.txt_myadvantage)
    EditText txt_jobneedadvantage;

    //下边是用户的对象类型
    @InjectView(R.id.circle_img)
    ImageView im_useravatar;
    @InjectView(R.id.txt_username)
    EditText txt_username;
    @InjectView(R.id.txt_phonum)
    EditText txt_phonum;
    @InjectView(R.id.tv_change)
    TextView txt_change;
    @InjectView(R.id.im_back)
    ImageView im_back;
    int jobneedid;
    JobneedBean jobneedBean;

    Boolean isenableEdit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobneeddetail);
        ButterKnife.inject(this);
        jobneedid=getIntent().getIntExtra("jobneedid",0);//如果没有这个数，设置默认值为0
        System.out.println("---获取的jobneedid内容测试---"+jobneedid);
        initView();
        getJobneedData();
        chagestate();//首先设置为不可编辑状态
    }

    //初始化组件的内容
    public void initView(){
        txt_change.setOnClickListener(this);
        im_back.setOnClickListener(this);

    }

    //获取数据内容，给jobneedBean赋值
    public void getJobneedData(){
        final JobneedPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(JobneedPageinterface.class);
        request.getJobneeddetail(jobneedid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<JobneedBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull EntityResponse<JobneedBean> jobneedBeanEntityResponse) {
                        jobneedBean=jobneedBeanEntityResponse.getObject();
                        System.out.println("------test-username---"+jobneedBean.getUsername());
                        initData(jobneedBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("----------wrong");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //利用jobneedBean给图形界面赋值
    public void initData(JobneedBean jobneedBean){
        System.out.println("------test2-username---"+jobneedBean.getUsername());
        txt_jobneedtitle.setText(jobneedBean.getJobneedtitle());
        txt_jobneedtype.setText(jobneedBean.getJobneedtype());
        txt_jobneedbftime.setText(jobneedBean.getBftime());
        txt_jobneedworktime.setText(jobneedBean.getWorktime());
        txt_jobneedcontent.setText(jobneedBean.getJobneedcontent());
        txt_jobneedadvantage.setText(jobneedBean.getMyadvantage());

        Glide.with(JobneeddetailActivity.this).load(jobneedBean.getJobimageuri()).into(im_useravatar);
        txt_username.setText(jobneedBean.getUsername());
        txt_phonum.setText(jobneedBean.getPhonum());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_change:
                if (txt_change.getText().toString().equals("编辑")){
                    txt_change.setText("保存更改");
                    txt_change.setBackgroundColor(Color.RED);
                    isenableEdit=true;
                    chagestate();//设置可编辑状态
                }else {//说明点击的是保存修改
                    new SweetAlertDialog(JobneeddetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定保存修改?")
                            .setContentText("这条兼职信息将更有吸引力！")
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
                                    updateJobneedpost();//确认保存修改
                                    sDialog.setTitleText("已更改")
                                            .setContentText("你的求职信息更具吸引力！")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }
                            })
                            .show();
                    txt_change.setText("编辑");
                    txt_change.setBackgroundColor(getColor(R.color.blue));
                    isenableEdit=false;
                    chagestate();//设置为不可编辑状态
                    //执行保存操作
                }
                //这个地方进入我的求职发布编辑界面
                break;
        }
    }

    //设置EditText是否为可编辑状态
    public void chagestate(){
        txt_jobneedtitle.setEnabled(isenableEdit);
        txt_jobneedbftime.setEnabled(isenableEdit);
        txt_jobneedworktime.setEnabled(isenableEdit);
        txt_jobneedcontent.setEnabled(isenableEdit);
        txt_jobneedadvantage.setEnabled(isenableEdit);
        txt_username.setEnabled(isenableEdit);
        txt_phonum.setEnabled(isenableEdit);
    }

    //更改修改后的我的求职发布信息
    public void updateJobneedpost(){
        JobneedBean jobneedBean=new JobneedBean();
        jobneedBean.setJobneedid(jobneedid);
        jobneedBean.setJobneedtitle(txt_jobneedtitle.getText().toString());
        jobneedBean.setBftime(txt_jobneedbftime.getText().toString());
        jobneedBean.setWorktime(txt_jobneedworktime.getText().toString());
        jobneedBean.setJobneedcontent(txt_jobneedcontent.getText().toString());
        jobneedBean.setMyadvantage(txt_jobneedadvantage.getText().toString());
        jobneedBean.setUsername(txt_username.getText().toString());
        jobneedBean.setPhonum(txt_phonum.getText().toString());

        final JobneedPageinterface request=RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(JobneedPageinterface.class);
        request.updateJobneedpost(jobneedBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("------success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("-----我的求职发布修改成功");
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
