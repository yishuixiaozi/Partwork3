package com.hhit.edu.partwork3;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SendmessageActivity extends AppCompatActivity {

    EditText edt_phonenumber;
    EditText edt_message;
    Button btn_send;
    SmsManager smsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS} , 0);
        String phone=getIntent().getStringExtra("phonenumber");
        edt_phonenumber= (EditText) findViewById(R.id.edt_phonenumber);
        edt_message= (EditText) findViewById(R.id.edt_message);
        btn_send= (Button) findViewById(R.id.btn_send);
        edt_phonenumber.setText(phone);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("------短信功能");
               String phone=edt_phonenumber.getText().toString();
                String message=edt_message.getText().toString();
                System.out.println("-----"+phone+message);
                PendingIntent pi=PendingIntent.getActivity(SendmessageActivity.this,0,new Intent(),0);
                smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, pi, null);
                Toast.makeText(SendmessageActivity.this,"发送短信成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
