package com.hhit.edu.partwork3;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.utils.Autjcode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class registerActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button registerBack;
    private Button registerCheck;
    private Button registerBtn;
    private EditText registerId;
    private EditText registerPassword;
    private EditText registerAuth;
    private EditText turePassword;
    private TextView registerBackText;
    private TextView registerIdText;
    private TextView registerPwText;
    private TextView turePwText;
    private TextView registerAuthText;
    private ImageView registerAuthimg;
    private String isPhone, isPassword, isTruePassword, Autecode, Autecodeimg;
    private int flagPhone, flagPassword, flagTruePassword, flagAutecode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();//隐藏标题栏的作用
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView(){
        //返回两个字以及返回箭头标志
        registerBack = (Button) findViewById(R.id.registerBack);
        registerBack.setOnClickListener(this);
        registerBackText = (TextView) findViewById(R.id.registerBackText);
        registerBackText.setOnClickListener(this);
        //换一张和注册按钮
        registerCheck = (Button) findViewById(R.id.registerCheck);
        registerCheck.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        //号码和密码
        registerId = (EditText) findViewById(R.id.registerId);
        registerId.setOnFocusChangeListener(this);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPassword.setOnFocusChangeListener(this);
        //验证码和确认密码
        registerAuth = (EditText) findViewById(R.id.registerAuth);
        registerAuth.setOnFocusChangeListener(this);
        registerAuth.setOnClickListener(this);
        turePassword = (EditText) findViewById(R.id.turePassword);
        turePassword.setOnFocusChangeListener(this);
        //验证码生成
        registerAuthimg = (ImageView) findViewById(R.id.registerAuthimg);
        registerAuthimg.setImageBitmap(Autjcode.getInstance().createBitmap());
        //提示框
        registerIdText = (TextView) findViewById(R.id.registerIdText);
        registerPwText = (TextView) findViewById(R.id.registerPwText);
        turePwText = (TextView) findViewById(R.id.turePwText);
        registerAuthText = (TextView) findViewById(R.id.registerAuthText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBack:
                registerActivity.this.finish();
                break;
            case R.id.registerBackText:
                registerActivity.this.finish();
                break;
            case R.id.registerAuth:
                registerAuth.setFocusable(true);
                registerAuth.setFocusableInTouchMode(true);
                registerAuth.requestFocus();
                registerAuth.findFocus();
                break;
            case R.id.registerCheck:
                registerAuthimg.setImageBitmap(Autjcode.getInstance()
                        .createBitmap());
                break;
            case R.id.registerBtn:
                Toast.makeText(registerActivity.this, "注册内容暂时没写",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isPhone = registerId.getText().toString();
        isPassword = registerPassword.getText().toString();
        isTruePassword = turePassword.getText().toString();
        Autecode = registerAuth.getText().toString();
        Autecodeimg = Autjcode.getInstance().getCode().toUpperCase();

        switch (v.getId()) {
            case R.id.registerId:
                if (hasFocus == false) {
                    // 手机号码的正则判断
                    Pattern pattern = Pattern.compile("^1[3,5,8]\\d{9}$");
                    Matcher matcher = pattern.matcher(isPhone);
                    if (matcher.find()) {
                        registerIdText.setVisibility(View.INVISIBLE);
                        flagPhone = 1;
                    } else {
                        if (registerId.length() != 0) {
                            registerIdText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case R.id.registerPassword:
                if (hasFocus == false) {
                    if ((isPassword.length() < 6 || isPassword.length() > 20)
                            && isPassword.length() != 0) {
                        registerPwText.setVisibility(View.VISIBLE);
                    } else {
                        registerPwText.setVisibility(View.INVISIBLE);
                        flagPassword = 1;
                    }
                }
                break;
            case R.id.turePassword:
                if (hasFocus == false) {
                    if (isTruePassword.equals(isPassword)) {
                        turePwText.setVisibility(View.INVISIBLE);
                        flagTruePassword = 1;
                    } else {
                        if (turePassword.length() != 0) {
                            turePwText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case R.id.registerAuth:
                if (hasFocus == false) {
                    // 判断验证码是否正确，toUpperCase()是不区分大小写
                    if (Autecode.toUpperCase().equals(Autecodeimg)) {
                        registerAuthText.setVisibility(View.INVISIBLE);
                        flagAutecode = 1;
                    } else {
                        if (registerAuth.length() != 0) {
                            registerAuthText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
