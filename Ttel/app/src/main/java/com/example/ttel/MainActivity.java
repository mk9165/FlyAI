package com.example.ttel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationBarView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //
//    하단바
    HomeFragment homeFragment;
    DialFragment dialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 하단바 구현
        homeFragment = new HomeFragment();
        dialFragment = new DialFragment();

        // 키패드 구현
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, dialFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setSelectedItemId(R.id.keypad);

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.today:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
//                    case R.id.contact:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
//                        return true;
                    case R.id.keypad:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, dialFragment).commit();
                        return true;
                    case R.id.recent:
                        Intent intent = new Intent(getApplicationContext(), CallListActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
//                    case R.id.option:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
//                        return true;
                }
                return false;
            }
        });

    }

    public void onFragmentChange(String phoneNum){
        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        startActivity(intent);
    }

    public void onFragmentChange2(){
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setSelectedItemId(R.id.recent);
    }

}