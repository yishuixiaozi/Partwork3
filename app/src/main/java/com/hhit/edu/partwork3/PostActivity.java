package com.hhit.edu.partwork3;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.bean.JobBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;

import java.util.Calendar;

import butterknife.InjectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    @InjectView(R.id.edt_title)
    EditText edt_title;//兼职标题
    TextView txt_jobtype;//兼职类型
    @InjectView(R.id.edt_worktime)
    EditText edt_worktime;//兼职时间段
    @InjectView(R.id.workplace)
    EditText edt_workplace;
    @InjectView(R.id.edt_peoplenum)
    EditText edt_peoplenum;//招聘人数
    @InjectView(R.id.edt_money)
    EditText edt_money;//薪资
    @InjectView(R.id.edt_username)
    EditText edt_username;//联系人
    @InjectView(R.id.edt_phonenum)
    EditText edt_phonenum;//联系电话
    @InjectView(R.id.edt_describe)
    EditText edt_describe;//兼职内容描述
    @InjectView(R.id.btn_post)
    Button btn_post;//确认提交
    @InjectView(R.id.txt_begintime)
    TextView txt_begintime;
    @InjectView(R.id.txt_endtime)
    TextView txt_endtime;
    @InjectView(R.id.txt_closetime)
    TextView txt_closetime;
    @InjectView(R.id.im_back)
    ImageView im_back;

    Spinner sp_paytype;
    Spinner sp_payway;
    Spinner sp_gender;
    Spinner sp_jobtype;
    String[] paytype;
    String[] payway;
    String[] gender;
    String[] jobtype;
    int mYear;
    int mMonth;
    int mDay;
    String userid;
    JobBean jobBean=new JobBean();
    int beigintime,endtime,closetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        userid=preferences.getString("userid","default");//获取用户当前用户id
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        initview();
        initarry();
    }
    /**
     * 初始化组件
     */
    public void initview(){
        btn_post.setOnClickListener(this);
        txt_begintime.setOnClickListener(this);
        txt_endtime.setOnClickListener(this);
        txt_closetime.setOnClickListener(this);
        im_back.setOnClickListener(this);
    }
    public void initarry(){
        paytype=getResources().getStringArray(R.array.paytype);
        payway=getResources().getStringArray(R.array.payway);
        gender=getResources().getStringArray(R.array.gender);
        jobtype=getResources().getStringArray(R.array.jobtype);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                postwork();
                break;
            case R.id.txt_begintime:
                new DatePickerDialog(this, onDateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.txt_endtime:
                new DatePickerDialog(this, onDateSetListener1, mYear, mMonth, mDay).show();
                break;
            case R.id.txt_closetime:
                new DatePickerDialog(this, onDateSetListener2, mYear, mMonth, mDay).show();
                break;
            case R.id.im_back://关闭当前内容回到上一个界面
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 兼职发布,并且封装对象
     */
    public void postwork(){
        System.out.println("最终值的测试内容--------------------");
        jobBean.setUserid(userid);
        jobBean.setUsername(edt_username.getText().toString());
        jobBean.setPhonenum(edt_phonenum.getText().toString());
        jobBean.setTitle(edt_title.getText().toString());
        int index1=sp_paytype.getSelectedItemPosition();
        jobBean.setPaymoney(edt_money.getText().toString()+paytype[index1]);
        index1=sp_payway.getSelectedItemPosition();
        jobBean.setPayway(payway[index1]);
        jobBean.setWorktime(edt_worktime.getText().toString());
        jobBean.setBeigintime(txt_closetime.getText().toString());
        jobBean.setPeoplenun(Integer.parseInt(edt_peoplenum.getText().toString()));
        index1=sp_jobtype.getSelectedItemPosition();
        jobBean.setJobtype(jobtype[index1]);
        jobBean.setBftime(txt_begintime.getText().toString()+"-"+txt_endtime.getText().toString());
        jobBean.setWorkdescribe(edt_describe.getText().toString());
        index1=sp_gender.getSelectedItemPosition();
        jobBean.setGender(gender[index1]);
        jobBean.setWorkplace(edt_workplace.getText().toString());

        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.addJobBean(jobBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull String s) {
                        Toast.makeText(PostActivity.this,"职位发布成功",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("职位发布出错");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
    //兼职开始时间
    private DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            String days;
            days=new StringBuffer().append(mMonth+1).append("月").append(mDay).append("日").toString();
            txt_begintime.setText(days);
            String beigintime1=new StringBuffer().append(mYear).append(mMonth+1).append(mDay).toString();
            beigintime= Integer.parseInt(beigintime1);//数组类型时间

        }
    };
    //兼职结束时间
    private DatePickerDialog.OnDateSetListener onDateSetListener1=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            String days;
            days=new StringBuffer().append(mMonth+1).append("月").append(mDay).append("日").toString();
            String beigintime1=new StringBuffer().append(mYear).append(mMonth+1).append(mDay).toString();
            endtime= Integer.parseInt(beigintime1);//数组类型时间
            if (endtime<beigintime){
                Toast.makeText(getApplication(),"请检查开始时间和结束时间",Toast.LENGTH_SHORT).show();
            }
            else {
                txt_endtime.setText(days);
            }

        }
    };
    //招聘结束时间
    private DatePickerDialog.OnDateSetListener onDateSetListener2=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            String days;
            days = new StringBuffer().append(mYear).append("年").
                    append(mMonth + 1).append("月").append(mDay).append("日").toString();
            String beigintime1=new StringBuffer().append(mYear).append(mMonth+1).append(mDay).toString();
            closetime= Integer.parseInt(beigintime1);//数组类型时间
            if(closetime>endtime){
                Toast.makeText(getApplication(),"招聘时间不正确，请检查",Toast.LENGTH_SHORT).show();
            }
            else {
                txt_closetime.setText(days);
            }

        }
    };

}
