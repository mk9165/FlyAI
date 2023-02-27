package com.example.ttel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;


public class HomeFragment extends Fragment {
    private static final int FILE_PICKER_CODE = 100;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton calling = root.findViewById(R.id.calling);
        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUploadFile();
            }
        });

        return root;
    }

    private void requestUploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_PICKER_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = getFilePathFromUri(uri);
                if (filePath != null) {
                    File file = new File(filePath);
                    FileUploadUtils.send2Server(file);
                } else {
                    Log.e("TAG", "Failed to get file path from URI");
                }
            }
        }
    }

    private String getFilePathFromUri(Uri uri) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
    }
}