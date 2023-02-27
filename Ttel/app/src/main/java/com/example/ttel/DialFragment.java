package com.example.ttel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class DialFragment extends Fragment implements View.OnClickListener {

    MainActivity activity;
    EditText et_result;

    public DialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dial, container, false);

        et_result = root.findViewById(R.id.et_result);
        et_result.setInputType(InputType.TYPE_CLASS_PHONE);
        et_result.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        et_result.setInputType(0);

        Button num_0 = root.findViewById(R.id.num_0);
        Button num_1 = root.findViewById(R.id.num_1);
        Button num_2 = root.findViewById(R.id.num_2);
        Button num_3 = root.findViewById(R.id.num_3);
        Button num_4 = root.findViewById(R.id.num_4);
        Button num_5 = root.findViewById(R.id.num_5);
        Button num_6 = root.findViewById(R.id.num_6);
        Button num_7 = root.findViewById(R.id.num_7);
        Button num_8 = root.findViewById(R.id.num_8);
        Button num_9 = root.findViewById(R.id.num_9);
        ImageButton back = root.findViewById(R.id.back);
        ImageButton calling = root.findViewById(R.id.calling);

        num_0.setOnClickListener(this);
        num_1.setOnClickListener(this);
        num_2.setOnClickListener(this);
        num_3.setOnClickListener(this);
        num_4.setOnClickListener(this);
        num_5.setOnClickListener(this);
        num_6.setOnClickListener(this);
        num_7.setOnClickListener(this);
        num_8.setOnClickListener(this);
        num_9.setOnClickListener(this);
        num_9.setOnClickListener(this);
        back.setOnClickListener(this);
        calling.setOnClickListener(this);

        return root;

    }

    @Override
    public void onClick(View v) {
        String current = et_result.getText().toString();

        switch (v.getId()) {
            case R.id.num_0:
                et_result.append("0");
                break;
            case R.id.num_1:
                et_result.append("1");
                break;
            case R.id.num_2:
                et_result.append("2");
                break;
            case R.id.num_3:
                et_result.append("3");
                break;
            case R.id.num_4:
                et_result.append("4");
                break;
            case R.id.num_5:
                et_result.append("5");
                break;
            case R.id.num_6:
                et_result.append("6");
                break;
            case R.id.num_7:
                et_result.append("7");
                break;
            case R.id.num_8:
                et_result.append("8");
                break;
            case R.id.num_9:
                et_result.append("9");
                break;
            case R.id.calling:
                activity.onFragmentChange(et_result.getText().toString());
                break;
            case R.id.back:
                et_result.setText(current.substring(0, current.length()-1));
                break;
        }
    }


}