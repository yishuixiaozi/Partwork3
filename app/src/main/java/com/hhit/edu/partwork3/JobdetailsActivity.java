package com.hhit.edu.partwork3;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobdetailsActivity extends AppCompatActivity {

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
    int id;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetails);
        ButterKnife.inject(this);//使用注解
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
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080/AndroidService/")//http://192.168.0.101 192.168.137.1
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        final HomePageInterface request=retrofit.create(HomePageInterface.class);
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
        Glide.with(JobdetailsActivity.this).load(user.getPhotouri()).into(circle_image);
    }

}