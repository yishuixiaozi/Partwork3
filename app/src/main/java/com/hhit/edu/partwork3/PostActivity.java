package com.hhit.edu.partwork3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edt_title;//兼职标题
    TextView txt_jobtype;//兼职类型
    EditText edt_worktime;//兼职时间段
    EditText edt_peoplenum;//招聘人数
    EditText edt_money;//薪资
    EditText edt_username;//联系人
    EditText edt_phonenum;//联系电话
    EditText edt_describe;//兼职内容描述
    Button btn_post;//确认提交

    Spinner sp_paytype;
    Spinner sp_payway;
    Spinner sp_gender;

    String[] paytype;
    String[] payway;
    String[] gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();
        initarry();
    }
    /**
     * 初始化组件
     */
    public void initview(){
        edt_title= (EditText) findViewById(R.id.edt_title);
        txt_jobtype= (TextView) findViewById(R.id.txt_jobtype);
        edt_worktime= (EditText) findViewById(R.id.edt_worktime);
        edt_peoplenum= (EditText) findViewById(R.id.edt_peoplenum);
        edt_money= (EditText) findViewById(R.id.edt_money);
        edt_username= (EditText) findViewById(R.id.edt_username);
        edt_phonenum= (EditText) findViewById(R.id.edt_phonenum);
        edt_describe= (EditText) findViewById(R.id.edt_describe);
        btn_post= (Button) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(this);
        sp_paytype= (Spinner) findViewById(R.id.sp_paytype);
        sp_payway= (Spinner) findViewById(R.id.sp_payway);
        sp_gender= (Spinner) findViewById(R.id.sp_gender);

    }

    public void initarry(){
        paytype=getResources().getStringArray(R.array.paytype);
        payway=getResources().getStringArray(R.array.payway);
        gender=getResources().getStringArray(R.array.gender);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                System.out.println("最终值的测试内容--------------------");
                System.out.println("兼职标题="+edt_title.getText().toString());
                System.out.println("兼职类型=");
                System.out.println("工作时间段"+edt_worktime.getText().toString());
                System.out.println("招聘人数"+edt_phonenum.getText().toString());
                int index1=sp_paytype.getSelectedItemPosition();
                System.out.println("薪资水品"+edt_money.getText().toString()+paytype[index1]);
                int index2=sp_payway.getSelectedItemPosition();
                System.out.println("结算方式"+payway[index2]);
                int index3=sp_gender.getSelectedItemPosition();
                System.out.println("性别要求"+gender[index3]);
                System.out.println("兼职描述"+edt_describe.getText().toString());
                break;
            default:
                break;
        }
    }
}
