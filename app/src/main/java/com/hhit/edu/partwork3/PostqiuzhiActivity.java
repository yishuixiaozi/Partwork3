package com.hhit.edu.partwork3;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

public class PostqiuzhiActivity extends AppCompatActivity implements View.OnClickListener{
    String myuserid;
    @InjectView(R.id.edt_title)
    EditText edt_jobneedtitle;
    @InjectView(R.id.sp_jobtype)
    Spinner sp_jobneedtype;
    @InjectView(R.id.edt_bftime)
    EditText edt_bftime;
    @InjectView(R.id.edt_worktime)
    EditText edt_worktime;
    @InjectView(R.id.edt_username)
    EditText edt_username;
    @InjectView(R.id.edt_phonenum)
    EditText edt_phonum;
    @InjectView(R.id.edt_describe)
    EditText edt_jobneedcontent;
    @InjectView(R.id.edt_myadvantage)
    EditText edt_myadvantage;
    @InjectView(R.id.btn_post)
    Button btn_post;
    @InjectView(R.id.im_back)
    ImageView im_back;

    String[] jobtype;
    JobneedBean jobneedBean=new JobneedBean();
    int index1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postqiuzhi);
        ButterKnife.inject(this);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");//获取用户当前用户id
        System.out.println("---------发布求职界面----myuserid----"+myuserid);
        initView();
        initArray();
    }

    /**
     * 初始化组件
     */
    public void initView(){
        btn_post.setOnClickListener(this);
        im_back.setOnClickListener(this);
    }

    /**
     * 初始化数组内容
     */
    public void initArray(){
        jobtype=getResources().getStringArray(R.array.jobtype);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                postmyjobneed();
                break;
            case R.id.im_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void postmyjobneed(){
        jobneedBean.setJobneedtitle(edt_jobneedtitle.getText().toString());
        index1=sp_jobneedtype.getSelectedItemPosition();
        jobneedBean.setJobneedtype(jobtype[index1]);
        jobneedBean.setBftime(edt_bftime.getText().toString());
        jobneedBean.setWorktime(edt_worktime.getText().toString());
        jobneedBean.setUsername(edt_username.getText().toString());
        jobneedBean.setPhonum(edt_phonum.getText().toString());
        jobneedBean.setUserid(myuserid);
        jobneedBean.setJobneedcontent(edt_jobneedcontent.getText().toString());
        jobneedBean.setMyadvantage(edt_myadvantage.getText().toString());

        final JobneedPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(JobneedPageinterface.class);
        request.postJobneed(jobneedBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("success--------");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        new SweetAlertDialog(PostqiuzhiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("哇哦...")
                                .setContentText("发布出了点小问题")
                                .show();
                    }
                    @Override
                    public void onComplete() {
                        new SweetAlertDialog(PostqiuzhiActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("干得好")
                                .setContentText("你成功发布了自己的职位需求")
                                .show();
                        finish();//关闭兼职发布页面
                    }
                });
    }
}
