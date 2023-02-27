package com.example.ttel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class RecordActivity extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 번호, 시간 받아오기
        Intent intent = getIntent();
        String phoneNum = intent.getStringExtra("phoneNum");
        String getmin = intent.getStringExtra("min");
        String getsec = intent.getStringExtra("sec");
        String filename = intent.getStringExtra("filename");

        TextView callnum = findViewById(R.id.callnum);
        callnum.setText(phoneNum);

        TextView min = findViewById(R.id.min);
        if (getmin.length() < 2) {
            getmin = "0" + getmin;
        }
        min.setText(getmin);
        TextView sec = findViewById(R.id.sec);
        if (getsec.length() < 2) {
            getsec = "0" + getsec;
        }
        sec.setText(getsec);

        User user = new User(phoneNum, getmin + ":" + getsec, "현재시각");


        // 서버통신
        File file = new File(filename);
        FileUploadUtils.send2Server(file);


        // 2.5초 후 메인화면으로 복귀
        Handler timer = new Handler();
        timer.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2500); //
    }

}
