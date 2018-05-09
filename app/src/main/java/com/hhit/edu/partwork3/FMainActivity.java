package com.hhit.edu.partwork3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FMainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_exit;
    Button btn_fabu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmain);
        initview();
    }
    public void initview(){
        btn_exit= (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        btn_fabu= (Button) findViewById(R.id.btn_fabu);
        btn_fabu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.remove("nickname");//这里是去除昵称：判断用户身份
                editor.remove("usertype");//这里是去除用户类型：判断进入的主界面
                editor.commit();
                startActivity(new Intent(this,LoginnewActivity.class));
                break;
            case R.id.btn_fabu:
                startActivity(new Intent(this,PostActivity.class));
                break;
            default:
                break;
        }
    }
}
