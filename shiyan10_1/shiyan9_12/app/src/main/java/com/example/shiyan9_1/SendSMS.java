package com.example.shiyan9_1;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class SendSMS extends AppCompatActivity implements View.OnClickListener {
    private EditText num;
    private EditText content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = (EditText) findViewById(R.id.Number);
        content = (EditText) findViewById(R.id.Content);
        Button sendNotice = (Button) findViewById(R.id.btnSend);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,NotificationActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取NotificationManager实例
        NotificationCompat.Builder builder;//创建通知对象
        if (Build.VERSION.SDK_INT >= 26) {//判断Android的版本
            NotificationChannel channel = new NotificationChannel(String.valueOf(1), "name",
                    NotificationManager.IMPORTANCE_HIGH);  //当Android版本大于等于8时，创建通知渠道（Notification Channels）
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(SendSMS.this, String.valueOf(1));//获取
        } else {
            builder = new NotificationCompat.Builder(SendSMS.this);//当版本低于8时使用
        }
        builder.setContentTitle("广东移动")
                .setContentText("您的余额不足，请及时充值！")
                .setWhen(System.currentTimeMillis())//指定通知被创建的时间
                .setSmallIcon(R.mipmap.ic_launcher)//使用小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);

        Notification notification = builder.build();
        manager.notify(1, notification);
    }


    public void send(View view) {
        String strNo = num.getText().toString();
        String strContent = content.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        //如果字数超过5,需拆分成多条短信发送
        if (strContent.length() > 5) {
            ArrayList<String> msgs = smsManager.divideMessage(strContent);
            for (String msg : msgs) {
                smsManager.sendTextMessage(strNo, null, msg, null, null);
            }
        } else {
            smsManager.sendTextMessage(strNo, null, strContent, null, null);
        }
        num.setText("");
        content.setText("");

        Toast.makeText(SendSMS.this, "短信发送完成", Toast.LENGTH_LONG).show();
    }
}