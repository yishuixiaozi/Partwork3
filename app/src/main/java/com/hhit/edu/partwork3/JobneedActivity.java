package com.hhit.edu.partwork3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * 招聘者查看求职者单个求职信息详细内容查看
 */
public class JobneedActivity extends AppCompatActivity implements View.OnClickListener{

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
    private final static int DIALOG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobneed);
        ButterKnife.inject(this);
        jobneedid=getIntent().getIntExtra("jobneedid",0);//如果没有这个数，设置默认值为0
        System.out.println("---获取的jobneedid内容测试---"+jobneedid);
        initView();
        getJobneedData();
        chagestate();//首先设置为不可编辑状态

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
        Glide.with(JobneedActivity.this).load(jobneedBean.getJobimageuri()).into(im_useravatar);
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
                //这个地方显示联系方式，并且进行沟通
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

    //选择沟通联系
    public void goutong(String hoddy){
        if (hoddy.equals("电话联系")){
            Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+jobneedBean.getPhonum()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (hoddy.equals("短信联系")){
            Intent intent=new Intent(this,SendmessageActivity.class);
            intent.putExtra("phonenumber",jobneedBean.getPhonum());
            startActivity(intent);
        }else {
            System.out.println("没有这个内容");
        }
    }
}
