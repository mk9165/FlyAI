package com.example.ttel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;

public class NotificationUtil extends BroadcastReceiver {
    private String channelId = "channel";
    private String channelName = "channelName";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //오레오 대응
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId);
        Intent notificationIntent = new Intent(context.getApplicationContext(), RecordListActivity.class);  // 알림 클릭 시 이동할 액티비티 지정
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        String NUMBER = intent.getStringExtra("NUMBER");
        String DATE = intent.getStringExtra("DATE");
        String TIME = intent.getStringExtra("TIME");

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "Recordings/" + NUMBER + "_" + DATE + "_" + TIME + ".m4a");   // 파일명
        String filename = file.getAbsolutePath();

        Intent deleteIntent = new Intent(context, NotificationUtil.class);
        deleteIntent.setAction("DELETE_FILE");
        deleteIntent.putExtra("FILE_PATH", filename);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle("통화상황 분석이 완료되었습니다.");
        builder.setContentText("알림을 누르면 분석 화면으로 이동합니다!");
        builder.setSmallIcon(R.drawable.vk_color);
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.vk_black, "확인", pendingIntent);
        builder.addAction(R.drawable.vk_black, "삭제", deletePendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        notificationManager.notify(0, builder.build());

        if (intent != null && intent.getAction().equals("DELETE_FILE")) {
            String filePath = intent.getStringExtra("FILE_PATH");
            fileDelete(context, filePath);
            return;
        }
    }

    public static String fileDelete(Context context, String filePath){
        try{
            File file = new File(filePath);
            if (file.exists()) {
                Log.d("TEST : ", filePath + "삭제");
                file.delete();}
            return "true";
        } catch (Exception e){
            Log.d("TEST : ", filePath);
            e.printStackTrace();
        }
        return "true";
    }

}