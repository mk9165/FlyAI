package com.example.ttel;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CallListActivity extends AppCompatActivity {

    private ListView audiolistView; // 리스트뷰
    private ArrayList<CallListActivity.Song_Item> songsList = null; // 데이터 리스트
    private CallListActivity.ListViewAdapter listViewAdapter = null; // 리스트뷰에 사용되는 ListViewAdapter
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);

        context = this.getBaseContext();
        songsList = new ArrayList<CallListActivity.Song_Item>(); // ArrayList 생성
        audiolistView = (ListView) findViewById(R.id.listView);
        listViewAdapter = new CallListActivity.ListViewAdapter(this); // Adapter 생성
        audiolistView.setAdapter(listViewAdapter); // 어댑터를 리스트뷰에 세팅
        audiolistView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        loadAudio();

        // 상단메뉴 구현
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu4){
                            Intent intent2 = new Intent(getApplicationContext(), RecordListActivity.class);
                            startActivity(intent2);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // 데이터 전달
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setSelectedItemId(R.id.recent);

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
//                    case R.id.today:
//                    case R.id.contact:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers2, homeFragment).commit();
//                        return true;
                    case R.id.keypad:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
//                    case R.id.recent:
//                        return true;
//                    case R.id.option:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers2, homeFragment).commit();
//                        return true;
                }
                return false;
            }
        });
    }
    class ViewHolder {
        public ImageView mImgAlbumArt;
        public TextView mTitle;
        public TextView mSubTitle;
        public TextView mDuration;
    }

    private class ListViewAdapter extends BaseAdapter {
        Context context;

        public ListViewAdapter(Context context) {
            this.context = context;
        }

        // 음악 데이터 추가를 위한 메소드
        public void addItem(String Title, long Duration,String DataPath) {
            CallListActivity.Song_Item item = new CallListActivity.Song_Item();
            item.setTitle(Title);
            item.setDuration(Duration);
            item.setDataPath(DataPath);
            songsList.add(item);
        }

        @Override
        public int getCount() {
            return songsList.size(); // 데이터 개수 리턴
        }

        @Override
        public Object getItem(int position) {
            return songsList.get(position);
        }

        // 지정한 위치(position)에 있는 데이터 리턴
        @Override
        public long getItemId(int position) {
            return position;
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CallListActivity.ViewHolder viewHolder;
            final Context context = parent.getContext();
            final Integer index = Integer.valueOf(position);

            // 화면에 표시될 View
            if(convertView == null){
                viewHolder = new CallListActivity.ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.song_item,parent,false);

                convertView.setBackgroundColor(0x00FFFFFF);
                convertView.invalidate();

                // 화면에 표시될 View 로부터 위젯에 대한 참조 획득
                viewHolder.mImgAlbumArt = (ImageView) convertView.findViewById(R.id.album_Image);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.txt_title);
                viewHolder.mSubTitle = (TextView) convertView.findViewById(R.id.txt_subtitle);
                viewHolder.mDuration = (TextView) convertView.findViewById(R.id.txt_duration);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CallListActivity.ViewHolder) convertView.getTag();
            }

            // PersonData 에서 position 에 위치한 데이터 참조 획득
            final CallListActivity.Song_Item songItem = songsList.get(position);

            String titlename = songItem.getTitle();
            int namelen = titlename.length();
            viewHolder.mTitle.setText(titlename.substring(0,namelen-14));
            Log.d("TEST : ", titlename.substring(namelen-5,namelen-3));
            int hh = Integer.parseInt(titlename.substring(namelen-4,namelen-2));
            int mm = Integer.parseInt(titlename.substring(namelen-2));
            String ampm;
            if (hh > 12){
                hh = hh - 12;
                ampm = "오후";
            } else{
                ampm = "오전";
            }
            viewHolder.mSubTitle.setText(ampm+" "+hh+":"+mm);

            int dur = (int) songItem.getDuration();
            int hrs = (dur / 3600000);
            int mns = (dur / 60000) % 60000;
            int scs = dur % 60000 / 1000;
            String songTime = String.format("%02d:%02d:%02d", hrs,  mns, scs);
            if(hrs == 0){
                songTime = String.format("%02d:%02d", mns, scs);
            }
            viewHolder.mDuration.setText(songTime);

            return convertView;
        }
    }


    public class Song_Item {
        private String Title; // 타이틀 정보
        private long Duration; // 재생시간
        private String DataPath; // 실제 데이터 위치

        public Song_Item() {
        }

        public String getTitle() {
            return Title;
        }
        public void setTitle(String title) {
            Title = title;
        }
        public long getDuration() {
            return Duration;
        }
        public void setDuration(long duration) {
            Duration = duration;
        }
        public void setDataPath(String dataPath) {
            DataPath = dataPath;
        }
    }

    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "== 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                long mDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                listViewAdapter.addItem(title,mDuration,datapath);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}