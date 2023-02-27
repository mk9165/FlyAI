package com.example.ttel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordListActivity extends AppCompatActivity {

    private ListView listView; // 리스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        listView = findViewById(R.id.my_listView);
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(RecordListActivity.this, CallListActivity.class);
                startActivity(backintent);
                finish();
            }
        });

        try {
            InputStream is = getApplicationContext().openFileInput("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
            String jsonStr = stringBuilder.toString();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<jsonModel>>(){}.getType();
            List<jsonModel> dataList = gson.fromJson(jsonStr, listType);

            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.result_item, dataList);
            listView.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jsonModel model = (jsonModel) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(RecordListActivity.this, ResultDetailActivity.class);
                intent.putExtra("model", model);
                startActivity(intent);
            }
        });


    }

}