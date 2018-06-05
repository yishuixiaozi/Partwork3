package com.hhit.edu.partwork3;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.CollectionBean;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.SignupBean;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.my_interface.SignupPageinterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobdetailsActivity extends AppCompatActivity implements View.OnClickListener{
    @InjectView(R.id.jobtitle)
    TextView jobtitle;//兼职标题
    @InjectView(R.id.paymoney)
    TextView paymoney;//薪资
    @InjectView(R.id.payway)
    TextView payway;//结算方式
    @InjectView(R.id.begintime)
    TextView begintime;//开始时间
    @InjectView(R.id.viewtimes)
    TextView viewtimes;//浏览次数
    @InjectView(R.id.peoplenum)
    TextView peoplenum;
    @InjectView(R.id.bftime)
    TextView bftime;
    @InjectView(R.id.worktime)
    TextView worktime;
    @InjectView(R.id.workplace)
    TextView workplace;
    @InjectView(R.id.tv_content)
    TextView tv_content;
    //用户相关信息组件
    @InjectView(R.id.tv_username)
    TextView tv_username;
    @InjectView(R.id.tv_company)
    TextView tv_company;
    @InjectView(R.id.tv_comnature)
    TextView tv_comnature;
    @InjectView(R.id.circle_img)
    ImageView circle_image;
    JobBean job;
    UserBean user;
    @InjectView(R.id.ll_map)
    LinearLayout ll_map;

    @InjectView(R.id.im_back)
    ImageView im_back;
    @InjectView(R.id.im_collection)
    ImageView im_collection;
    @InjectView(R.id.tv_signup)
    TextView tv_singup;
    @InjectView(R.id.tv_goutong)
    TextView tv_goutong;
    int id;
    String myuserid;//这里代表登录用户的id
    String userid;//这个地方需要注意，我的表已经改为String类型了
    String tag="no";

    private final static int DIALOG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetails);
        ButterKnife.inject(this);//使用注解
        tv_singup.setOnClickListener(this);
        tv_goutong.setOnClickListener(this);
        ll_map.setOnClickListener(this);
        im_back.setOnClickListener(this);
        im_collection.setOnClickListener(this);
        id=getIntent().getIntExtra("id",0);//依据Id查询兼职的详细信息
        userid=getIntent().getStringExtra("userid");//依据userId查询用户的相关信息
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        myuserid=preferences.getString("userid","default");
        System.out.println("报名myuserid---------测试"+myuserid+"userid="+userid);
        //System.out.println("获取的兼职id的值是-----"+id);
        //System.out.println("获取的userid的值是----"+userid);
        getData();
    }

    /**
     * 查询数据的内容的
     */
    public void getData(){
        System.out.println("获取兼职详细信息");
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.getJobById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EntityResponse<JobBean>>() {
                    @Override
                    public void accept(EntityResponse<JobBean> jobBeanEntityResponse) throws Exception {
                        job=jobBeanEntityResponse.getObject();
                        initjobData(job);
                    }
                });
        System.out.println("用户相关信息-----userid------>"+userid);
        request.getUserById(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EntityResponse<UserBean>>() {
                    @Override
                    public void accept(EntityResponse<UserBean> userBeanEntityResponse) throws Exception {
                        user=userBeanEntityResponse.getObject();
                        inituserData(user);
                    }
                });

        request.getCollectionTag(myuserid,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<CollectionBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull EntityResponse<CollectionBean> collectionBeanEntityResponse) {
                        tag=collectionBeanEntityResponse.getMsg();
                        System.out.println("tag----------------------->="+tag);
                        //设置图片格式内容
                        if (tag.equals("yes")){
                            im_collection.setImageResource(R.drawable.collected_icon);//设置为收藏状态
                        }
                        else {
                            im_collection.setImageResource(R.drawable.collect_icon);//设置为选中
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("获取收藏信息失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        final SignupPageinterface request1=RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(SignupPageinterface.class);
        //判断是否已经报名，设置报名的字体样式
        /*
        *
        * 是否要加入报名人数的设置，不然就没有意义了。看时间吧
        * */
        request1.searchSignup(userid,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<SignupBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("报名信息搜索中");
                    }
                    @Override
                    public void onNext(@NonNull EntityResponse<SignupBean> signupBeanEntityResponse) {
                        if (signupBeanEntityResponse.getMsg().equals("yes")){
                            tv_singup.setText("已报名");
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("获取报名信息错误");
                    }
                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 初始化兼职信息内容的
     */
    public void initjobData(JobBean job){
        jobtitle.setText(job.getTitle());
        paymoney.setText(job.getPaymoney());
        payway.setText(job.getPayway());
        begintime.setText(job.getBeigintime());
        viewtimes.setText(String.valueOf(job.getViewtimes()));
        peoplenum.setText(String.valueOf(job.getPeoplenun()));
        bftime.setText(job.getBftime());
        worktime.setText(job.getWorktime());
        workplace.setText(job.getWorkplace());
        tv_content.setText(job.getWorkdescribe());
    }

    /**
     * 初始化用户的信息
     * @param user
     */
    public void inituserData(UserBean user){
        tv_username.setText(user.getUsername());
        tv_company.setText(user.getCompany());
        tv_comnature.setText(user.getComnature());
        System.out.println("------uri获取----"+user.getPhotouri());
        Glide.with(JobdetailsActivity.this).load(user.getPhotouri()).into(circle_image);
    }

    /**
     * 各点击事件响应
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_map://点击的是路线选择
                Intent intent=new Intent(this,NewActivity.class);
                intent.putExtra("workplace",job.getWorkplace());
                startActivity(intent);
                break;
            case R.id.im_collection:
                collection();
                break;
            case R.id.im_back:
                finish();//关闭当前页，回到上一个页面
                break;
            case R.id.tv_signup:
                //这里弹出diaog进行一个选择后然后处理相关的内容
                jobsignup();
                break;
            case R.id.tv_goutong:
                //
                showDialog(DIALOG);
                break;
            default:
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
            Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+job.getPhonenum()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (hoddy.equals("短信联系")){
            Intent intent=new Intent(this,SendmessageActivity.class);
            intent.putExtra("phonenumber",job.getPhonenum());
            startActivity(intent);
        }else {
            System.out.println("没有这个内容");
        }
    }



    public void jobsignup(){
        if (tv_singup.getText().toString().equals("已报名")){
            Toast.makeText(this,"您已经报名，无法再次报名！",Toast.LENGTH_SHORT).show();
        }else {
            //执行数据库添加，并且将tv_singup命名为已报名，
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate=simpleDateFormat.format(date);
            SignupBean signupBean=new SignupBean();
            signupBean.setJobid(job.getId());
            signupBean.setUserid(myuserid);//这个地方错了，不能是user.getUserid,这里的user是发布者
            signupBean.setTime(currentDate);
            signupBean.setTitle(job.getTitle());
            signupBean.setPaymoney(job.getPaymoney());
            signupBean.setPayway(job.getPayway());
            signupBean.setWorktime(job.getWorktime());
            signupBean.setJobimageuri(job.getJobimageuri());
            final SignupPageinterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(SignupPageinterface.class);
            request.addSignup(signupBean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            System.out.println("处理中");
                        }

                        @Override
                        public void onNext(@NonNull String s) {
                            tv_singup.setText("已报名");

                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            new SweetAlertDialog(JobdetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("某些地方出了问题！")
                                    .show();
                        }
                        @Override
                        public void onComplete() {
                            new SweetAlertDialog(JobdetailsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                    .setTitleText("Sweet!")
                                    .setContentText("大侠!揭榜成功！")
                                    .setCustomImage(R.drawable.custom_img)
                                    .show();
                        }
                    });

        }
    }
    /**
     * 判断操作
     */
    public void collection(){
        if (tag.equals("yes")){
            //执行删除，取消收藏
            tag="no";
            System.out.println("取消收藏");
            deletecollection();

        }
        else {
            System.out.println("执行收藏");
            tag="yes";
            jobcollection();
            //执行收藏，添加内容
        }
    }

    public void deletecollection(){
        //执行删除
        //重新设置图标内容
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.deleteCollection(userid,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<CollectionBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull EntityResponse<CollectionBean> collectionBeanEntityResponse) {
                        if(collectionBeanEntityResponse.getMsg().equals("success")){
                            im_collection.setImageResource(R.drawable.collect_icon);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        new SweetAlertDialog(JobdetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("取消收藏信息出错！")
                                .show();
                    }
                    @Override
                    public void onComplete() {
                        new SweetAlertDialog(JobdetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("取消收藏成功")
                                .show();
                    }
                });

    }
    /**
     * 职位收藏,
     */
    public void jobcollection(){
        System.out.println("jobid="+job.getId());
        System.out.println("userid="+user.getUserid());
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate=simpleDateFormat.format(date);
        System.out.println("系统时间获取="+currentDate);
        CollectionBean collection=new CollectionBean();
        collection.setJobid(job.getId());
        collection.setUserid(myuserid);
        collection.setTime(currentDate);
        collection.setTitle(job.getTitle());
        collection.setPaymoney(job.getPaymoney());
        collection.setPayway(job.getPayway());
        collection.setWorktime(job.getWorktime());
        collection.setJobimageuri(job.getJobimageuri());
        //开始执行收藏插入操作
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.addCollection(collection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("执行中....");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        im_collection.setImageResource(R.drawable.collected_icon);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(JobdetailsActivity.this,"收藏失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        new SweetAlertDialog(JobdetailsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText("Sweet!")
                                .setContentText("收藏信息成功")
                                .setCustomImage(R.drawable.custom_img)
                                .show();
                    }
                });

    }
}
