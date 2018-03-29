package com.hhit.edu.partwork3;

import android.Manifest;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhit.edu.utils.Autjcode;

public class findpasswordActivity extends AppCompatActivity implements View.OnClickListener{

    //找回密码里边的内容
    private Button findPasswordBack;
    private Button findPasswordCheck;
    private Button findPasswordBtn;
    private EditText findPasswId;
    private EditText findPasswAuth;
    private ImageView findPasswordimg;
    private TextView findPasswordText;
    private TextView findPasswAuthText;
    private TextView findPasswIdText;
    private String Autecode, Autecodeimg;
    private String Id;
    private int myflagId, myflagAutn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        //这个应该是允许这个类进行发短信的内容
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS} , 1);
        getSupportActionBar().hide();//隐藏标题栏的作用

        initView();
    }

    /**
     * 控件对象初始化
     */
    private void initView(){
        findPasswordBack = (Button) findViewById(R.id.findPasswordBack);
        findPasswordBack.setOnClickListener(this);
        findPasswordCheck = (Button) findViewById(R.id.findPasswordCheck);
        findPasswordCheck.setOnClickListener(this);
        findPasswordBtn = (Button) findViewById(R.id.findPasswordBtn);
        findPasswordBtn.setOnClickListener(this);
        findPasswordText = (TextView) findViewById(R.id.findPasswordText);
        findPasswordText.setOnClickListener(this);
        findPasswIdText = (TextView) findViewById(R.id.findPasswIdText);
        findPasswAuthText = (TextView) findViewById(R.id.findPasswAuthText);
        findPasswId = (EditText) findViewById(R.id.findPasswId);
        //手机号码聚焦改变事件
        findPasswId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Id=findPasswId.getText().toString();
                if (hasFocus=false){//移动位置了
                    /*
                    * 1.查询数据库
                    * 2.若果数据库没有这个号码，后面TextView显示（用户不存在）
                    * 3.不然的话哪个TextView就不显示*/
                }
            }
        });
        //验证码聚焦变化
        findPasswAuth = (EditText) findViewById(R.id.findPasswAuth);
        findPasswAuth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {//移开焦点
                    Autecode = findPasswAuth.getText().toString();//获取输入验证码
                    Autecodeimg = Autjcode.getInstance().getCode()
                            .toUpperCase();//验证码图片对象
                    if (Autecode.toUpperCase().equals(Autecodeimg)) {//验证码图==输入验证码
                        findPasswAuthText.setVisibility(View.INVISIBLE);
                        myflagAutn = 1;
                    } else {
                        if (findPasswAuth.length() != 0) {
                            findPasswAuthText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        findPasswAuth.setOnClickListener(this);
        findPasswordimg = (ImageView) findViewById(R.id.findPasswordimg);
        // 生成验证码
        findPasswordimg.setImageBitmap(Autjcode.getInstance().createBitmap());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findPasswordBack://点击的是返回
                findpasswordActivity.this.finish();
                break;
            case R.id.findPasswordText://点击的是返回
                findpasswordActivity.this.finish();
                break;
            case R.id.findPasswordCheck://点击的是重置验证码
                findPasswordimg.setImageBitmap(Autjcode.getInstance()
                        .createBitmap());
                break;
            case R.id.findPasswAuth://点击的是输入验证码
                // EditText重新获取焦点
                findPasswAuth.setFocusable(true);
                findPasswAuth.setFocusableInTouchMode(true);
                findPasswAuth.requestFocus();
                findPasswAuth.findFocus();
                break;
            case R.id.findPasswordBtn://点击的是确认
               /* System.out.println("测试---------------------");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("18451111598",null,"您的密码为"+"123456",null,null);*/
                //参数依次为：收信人号码，发信人号码，发送内容，发送状态，接受状态
                System.out.println("测试完成");
                findPasswAuth.setFocusable(false);
                if (myflagId == 1 && myflagAutn == 1) {
                    Id = findPasswId.getText().toString();
                    /*String phonecon = new MyloginCursor(
                            FindPasswordActivity.this.helper.getReadableDatabase())
                            .find(Id).toString();*/
                    /*String content[] = phonecon.split(",");
                    String phonenum = findPasswId.getText().toString().trim();*/
                    // 发送短信
                    //SmsManager smsManager = SmsManager.getDefault();
                    /*smsManager.sendTextMessage(phonenum, null, "您的账号密码为"
                            + content[2], null, null);*/
                    //smsManager.sendTextMessage("18451111598",null,"您的密码为"+"123456",null,null);
                }
                break;
            default:
                break;
        }
    }
}
