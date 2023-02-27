package com.example.ttel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultDetailActivity extends AppCompatActivity {

    jsonModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        getSelectresult();
        setValues();

        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(ResultDetailActivity.this, RecordListActivity.class);
                startActivity(backintent);
                finish();
            }
        });
    }

    private void setValues(){
        DecimalFormat df = new DecimalFormat("00");
        TextView phoneNum = findViewById(R.id.phoneNum);
        phoneNum.setText(model.getInfo().getNumber());

        ProgressBar happyBar = findViewById(R.id.happy);
        ProgressBar angryBar = findViewById(R.id.angry);
        ProgressBar fearBar = findViewById(R.id.fear);
        ProgressBar sadBar = findViewById(R.id.sad);
        happyBar.setProgress((int) (model.getRatio().getPositive() * 100));
        angryBar.setProgress((int) (model.getRatio().getAngry() * 100));
        fearBar.setProgress((int) (model.getRatio().getFear() * 100));
        sadBar.setProgress((int) (model.getRatio().getSad() * 100));

        TextView happypercent = findViewById(R.id.happypercent);
        TextView angrypercent = findViewById(R.id.angrypercent);
        TextView fearpercent = findViewById(R.id.fearpercent);
        TextView sadpercent = findViewById(R.id.sadpercent);
        happypercent.setText(String.valueOf(df.format(model.getRatio().getPositive()*100)) + "%");
        angrypercent.setText(String.valueOf(df.format(model.getRatio().getAngry()*100)) + "%");
        fearpercent.setText(String.valueOf(df.format(model.getRatio().getFear()*100)) + "%");
        sadpercent.setText(String.valueOf(df.format(model.getRatio().getSad()*100)) + "%");

        TextView keyword = findViewById(R.id.keyword);
        keyword.setText(model.getKeyword());

        TextView summary = findViewById(R.id.summary);
        summary.setText(model.getSummary());
    }


    private void getSelectresult(){
        Intent intent = getIntent();
        model = (jsonModel) intent.getSerializableExtra("model");
    }
}