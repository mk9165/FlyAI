package com.example.ttel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CallActivity extends AppCompatActivity {

    private Chronometer chronometer;
    int Record = 0;

    MediaRecorder recorder;
    String filename;
    String date;
    String phoneNum;

    // 파일명 날짜
    private String dateName(long dateTaken){
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMdd_HHmm");
        return dateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ImageButton record = findViewById(R.id.record);
        TextView recordText = findViewById(R.id.recordText);
        ImageView voicekeeper = findViewById(R.id.voicekeeper);


        // 번호 받아오기
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");
        TextView callnum = findViewById(R.id.callnum);
        callnum.setText(phoneNum);

        // 녹음버튼
        record.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if ( Record == 0 ){
                    record.setImageResource(R.drawable.select_record);
                    recordText.setTextColor(Color.parseColor("#2196F3"));
                    voicekeeper.setImageResource(R.color.white);
                    Record = 1;
                } else{
                    record.setImageResource(R.drawable.ic_baseline_voicemail_24);
                    recordText.setTextColor(Color.parseColor("darkgray"));
                    voicekeeper.setImageResource(R.drawable.vk_color);
                    Record = 0;
                } }
        });

        // 녹음파일
        date = dateName(System.currentTimeMillis());
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "Recordings/" + phoneNum + "_" + date + ".m4a");   // 파일명
        filename = file.getAbsolutePath();

        // 효과음 후 전화연결, 통화시간 측정 (효과음 시간 맞춰서 조절)
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.phone_calling_tone);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                chronometer = findViewById(R.id.stopwatch);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                // 녹음시작
                startRecording();
                voicekeeper.setImageResource(R.drawable.vk_color);
            }
        }, 7000); // 6초

        //  통화 종료
        ImageButton endcall = findViewById(R.id.endcalling);
        endcall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chronometer.stop();
                long pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                int time = (int) (pauseOffset / 1000);
                int min = time % (60*60) / 60;
                int sec = time % 60;

                //녹음종료
                stopRecording(phoneNum);

                Intent callintent = new Intent(CallActivity.this, RecordActivity.class);
                callintent.putExtra("phoneNum", phoneNum);
                callintent.putExtra("min", String.valueOf(min));
                callintent.putExtra("sec", String.valueOf(sec));
                callintent.putExtra("sec", String.valueOf(sec));
                callintent.putExtra("filename", String.valueOf(filename));

                recorder = null;
                startActivity(callintent);
                finish();
            }
        });
    }

    public void startRecording(){
        recorder = new MediaRecorder();

        /* MediaRecorder 설정하기 */
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);     // 마이크로 입력 받기
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);    // MPEG4 포맷으로 지정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   // 디폴트 인코터 사용
        recorder.setOutputFile(filename);       // 결과물 파일을 설정하는데 사용

        try{
            recorder.prepare();
            recorder.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void stopRecording(String phoneNum) {
        recorder.stop();
        recorder.release(); // 리소스 해제하는 역할

        ContentValues values = new ContentValues(10);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, phoneNum + "_" + date + ".m4a");
        values.put(MediaStore.Audio.Media.IS_PENDING, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 0);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4");

        ContentResolver contentResolver = getContentResolver();
        Uri item = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

//        recorder = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity","권한 허용 : " + permissions[i]);
                }
            }
        }


    }}