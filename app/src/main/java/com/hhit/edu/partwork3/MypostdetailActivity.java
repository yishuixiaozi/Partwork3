package com.hhit.edu.partwork3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobBean;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MypostdetailActivity extends AppCompatActivity implements View.OnClickListener{

    @InjectView(R.id.jobtitle)
    TextView tv_title;
    @InjectView(R.id.paymoney)
    EditText edt_paymoney;
    @InjectView(R.id.payway)
    EditText edt_payway;
    @InjectView(R.id.begintime)
    EditText edt_beigintime;
    @InjectView(R.id.peoplenum)
    EditText edt_peoplenum;
    @InjectView(R.id.bftime)
    EditText edt_bftime;
    @InjectView(R.id.worktime)
    EditText edt_worktime;
    @InjectView(R.id.edt_content)
    EditText edt_content;
    @InjectView(R.id.workplace)
    EditText edt_workplace;

    @InjectView(R.id.tv_change)
    TextView tv_change;

    @InjectView(R.id.tv_username)
    EditText tv_username;
    @InjectView(R.id.tv_phone)
    EditText tv_phone;
    boolean isenableEdit=false;
    int jobid;
    String edityes;
    @InjectView(R.id.im_back)
    ImageView im_back;
    @InjectView(R.id.tv_reaction)
    TextView tv_reaction;
    JobBean jobBean=new JobBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypostdetail);
        ButterKnife.inject(this);
        tv_change.setOnClickListener(this);
        im_back.setOnClickListener(this);
        jobid=getIntent().getIntExtra("jobid",0);
        edityes=getIntent().getStringExtra("edityes");
        if (edityes.equals("yes")){
            tv_change.setVisibility(View.GONE);
            tv_reaction.setVisibility(View.GONE);
        }
        getJobdetaildata();
        chagestate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_change:
                if (tv_change.getText().toString().equals("编辑")){
                    tv_change.setText("保存更改");
                    tv_change.setBackgroundColor(Color.RED);
                    isenableEdit=true;
                    chagestate();//设置可编辑状态
                }else {//开始保存工作
                    new SweetAlertDialog(MypostdetailActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                    updateJobpost();//确认保存修改
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
                    tv_change.setText("编辑");
                    tv_change.setBackgroundColor(getColor(R.color.blue));
                    isenableEdit=false;
                    chagestate();//设置为不可编辑状态
                }
                break;
        }
    }

    //改变编辑状态
    public void chagestate(){
        edt_paymoney.setEnabled(isenableEdit);
        edt_payway.setEnabled(isenableEdit);
        edt_beigintime.setEnabled(isenableEdit);
        edt_bftime.setEnabled(isenableEdit);
        edt_worktime.setEnabled(isenableEdit);
        edt_content.setEnabled(isenableEdit);
        edt_peoplenum.setEnabled(isenableEdit);
        edt_workplace.setEnabled(isenableEdit);
        tv_username.setEnabled(isenableEdit);
        tv_phone.setEnabled(isenableEdit);
    }

    //获取数据内容进行填充
    public void getJobdetaildata(){
        System.out.println("获取兼职详细信息");
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobById(jobid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EntityResponse<JobBean>>() {
                    @Override
                    public void accept(EntityResponse<JobBean> jobBeanEntityResponse) throws Exception {
                        jobBean=jobBeanEntityResponse.getObject();
                        initjobData(jobBean);
                    }
                });
    }


    //数据值的填充
    public void initjobData(JobBean jobBean){
        tv_title.setText(jobBean.getTitle());
        edt_paymoney.setText(jobBean.getPaymoney());
        edt_payway.setText(jobBean.getPayway());
        edt_beigintime.setText(jobBean.getBeigintime());
        edt_bftime.setText(jobBean.getBftime());
        edt_worktime.setText(jobBean.getWorktime());
        edt_content.setText(jobBean.getWorkdescribe());
        String peoplenum= String.valueOf(jobBean.getPeoplenun());
        edt_peoplenum.setText(peoplenum);
        edt_workplace.setText(jobBean.getWorkplace());
        tv_username.setText(jobBean.getUsername());
        tv_phone.setText(jobBean.getPhonenum());
    }
    //更改编辑后的内容
    public void updateJobpost(){
        jobBean.setId(jobid);
        //jobBean.setTitle(tv_title.getText().toString());
        jobBean.setPaymoney(edt_paymoney.getText().toString());
        jobBean.setPayway(edt_payway.getText().toString());
        jobBean.setBeigintime(edt_beigintime.getText().toString());
        jobBean.setBftime(edt_bftime.getText().toString());
        jobBean.setWorktime(edt_worktime.getText().toString());
        jobBean.setWorkdescribe(edt_content.getText().toString());
        jobBean.setPeoplenun(Integer.parseInt(edt_peoplenum.getText().toString()));//EditText设置为只能输入数字
        jobBean.setWorkplace(edt_workplace.getText().toString());
        jobBean.setUsername(tv_username.getText().toString());
        jobBean.setPhonenum(tv_phone.getText().toString());
        //修改内容的保存

        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.updateJobpost(jobBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("数据更新中...");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("数据更新成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("数据更新失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
