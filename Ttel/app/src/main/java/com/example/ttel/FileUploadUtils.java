package com.example.ttel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import com.microsoft.azure.storage.credentials.CredentialOptions;

public class FileUploadUtils {

    static String filename;

    public static void send2Server(File file){
        // 아래에 스토리지, 컨테이너 이름 삽입
        String storageAccount = "";
        String containerName = "";

        // 아래에 SAS Token 삽입
        String sasToken ="SAS Token";
        String url = "https://" + storageAccount + ".blob.core.windows.net/" + containerName + "/" + file.getName() + "?" + sasToken;


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp4"), file);

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("Content-Type", "audio/mp4")
                .addHeader("x-ms-blob-type", "BlockBlob")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String test = response.body().string();
                Log.d("TEST : ", test);
                filename = file.getName();
                filename = filename.substring(0, filename.length()-4);
                new CallFunctionTask().execute(filename);

            }
        });
    }

    private static class CallFunctionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                // 아래에 URL 삽입
                URL url = new URL("Azure blob storage URL"+filename);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } else {
                    Log.e("Response code", "Response code: " + responseCode);
                    Thread.sleep(10000);    // 10초 뒤 pooling
                    return null;
                }
            } catch (Exception e) {
                Log.e("Error message", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                // retry
                new CallFunctionTask().execute();
            } else {
                Log.d("TEST : ", result);
                int check = 0;  // 저장여부

                try {
                    JSONObject resultjson = new JSONObject(result);
                    check = resultjson.getInt("SAVE");
                    JSONObject INFO = resultjson.getJSONObject("INFO");
                    String NUMBER = INFO.getString("NUMBER");
                    String DATE = INFO.getString("DATE");
                    String TIME = INFO.getString("TIME");

                    if (check == 1){    // 녹음 상황 감지 -> 파일 저장, json 수정
                        Log.d("TEST : ", "파일 저장");
//                        JSONArray myJsonArray = new JSONArray();
//                        myJsonArray.put(resultjson);
//                        FileOutputStream outputStream = MyApplication.getAppContext().openFileOutput("data.json", Context.MODE_PRIVATE);
//                        outputStream.write(myJsonArray.toString().getBytes());
//                        outputStream.close();

                        File filesDir = MyApplication.getAppContext().getFilesDir();
                        File file = new File(filesDir, "data.json");

                        if (file.exists()) {
                            // 파일이 존재하는 경우
                            FileInputStream inputStream = MyApplication.getAppContext().openFileInput("data.json");
                            byte[] bytes = new byte[inputStream.available()];
                            inputStream.read(bytes);
                            inputStream.close();
                            String jsonString = new String(bytes);
                            JSONArray myJsonArray = new JSONArray(jsonString);

                            myJsonArray.put(resultjson);
                            FileOutputStream outputStream = MyApplication.getAppContext().openFileOutput("data.json", Context.MODE_PRIVATE);
                            outputStream.write(myJsonArray.toString().getBytes());
                            outputStream.close();
                        } else {
                            // 파일이 존재하지 않는 경우
                            JSONArray myJsonArray = new JSONArray();
                            myJsonArray.put(resultjson);
                            FileOutputStream outputStream = MyApplication.getAppContext().openFileOutput("data.json", Context.MODE_PRIVATE);
                            outputStream.write(myJsonArray.toString().getBytes());
                            outputStream.close();
                        }

                        NotificationUtil notificationUtil = new NotificationUtil();
                        Intent intent = new Intent(MyApplication.getAppContext(), NotificationUtil.class);

                        intent.putExtra("NUMBER", NUMBER);
                        intent.putExtra("DATE", DATE);
                        intent.putExtra("TIME", TIME);

                        notificationUtil.onReceive(MyApplication.getAppContext(), intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else{           // 녹음 불필요 -> 파일 삭제
                        Log.d("TEST : ", "파일 삭제");
                        File sdcard = Environment.getExternalStorageDirectory();
                        File file = new File(sdcard, "Recordings/" + NUMBER + "_" + DATE + "_" + TIME + ".m4a");
                        String filePath = file.getAbsolutePath();
                        file = new File(filePath);
                        if (file.exists()) {
                            file.delete();}
                    }

            } catch (Exception e) {
                    Log.e("error json", e.getMessage(), e);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            // doInBackground() 메서드 실행 전에 호출되는 메서드
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // doInBackground() 메서드에서 publishProgress() 메서드를 호출하여 UI 스레드로 업데이트를 보낼 때 호출되는 메서드
        }
    }


}

