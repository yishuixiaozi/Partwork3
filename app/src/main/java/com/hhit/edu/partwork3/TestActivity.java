package com.hhit.edu.partwork3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SharedPreferences preferences=getSharedPreferences("mydatabase",MODE_PRIVATE);
        String username=preferences.getString("username","");
        if ("".equals(username)){
            System.out.println("第一次用户信息是为空的");
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        System.out.println("清空数据之前---------------TestActivity"+username);
       /* SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.commit();*/
    }

}
