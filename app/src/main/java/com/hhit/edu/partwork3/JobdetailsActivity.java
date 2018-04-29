package com.hhit.edu.partwork3;
import android.content.Intent;
import android.media.Image;
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
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
    int id;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetails);
        ButterKnife.inject(this);//使用注解
        ll_map.setOnClickListener(this);
        im_back.setOnClickListener(this);
        im_collection.setOnClickListener(this);
        id=getIntent().getIntExtra("id",0);//依据Id查询兼职的详细信息
        userid=getIntent().getIntExtra("userid",1);//依据userId查询用户的相关信息
        System.out.println("获取的id的值是-----"+id);
        System.out.println("获取的userid的值是----"+userid);
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
                jobcollection();
                break;
            case R.id.im_back:
                finish();//关闭当前页，回到上一个页面
                break;
        }
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
        collection.setUserid(user.getUserid());
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
                        System.out.println("success,nothing to do");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(JobdetailsActivity.this,"收藏失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(JobdetailsActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
