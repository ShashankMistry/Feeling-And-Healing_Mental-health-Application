package com.shashank.mentalhealth.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shashank.mentalhealth.DB.DBHelper;
import com.shashank.mentalhealth.R;

import java.util.ArrayList;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    TextView tv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;
    int flag = 0, p = 0;
    Intent intent;
    private int correct = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Quiz");
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView textView = findViewById(R.id.DispName);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(10);
        intent = getIntent();
        DBHelper dbHelper = new DBHelper(this, null, 1);
        ArrayList<String> questions = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        dbHelper.fetchQuestions(intent.getStringExtra("test"), questions);
//        Toast.makeText(this, questions.size()+""+ intent.getStringExtra("test"), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        String Name;
        if (name != null) {
            Name = name;
        } else {
            String withAt = sharedPreferences.getString("edit", "Friend");
            String[] email = withAt.split("@");
            Name = email[0];
        }
        textView.setText("Hello , \n" + Name);

        submitbutton = findViewById(R.id.button3);
        quitbutton = findViewById(R.id.buttonquit);
        tv = findViewById(R.id.tvque);
        radio_g = findViewById(R.id.answersgrp);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        tv.setText(questions.get(flag));
        rb1.setText("NOT AT ALL");
        rb2.setText("SEVERAL DAYS");
        rb3.setText("MORE THAN HALF THE DAYS");
        rb4.setText("NEARLY EVERY DAY");
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton Ans = findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = Ans.getText().toString();
                flag++;

                switch (ansText) {
                    case "SEVERAL DAYS":
                        correct++;
                        break;
                    case "MORE THAN HALF THE DAYS":
                        correct = correct + 2;
                        break;
                    case "NEARLY EVERY DAY":
                        correct = correct + 3;
                        break;
                    default:
                        break;
                }
                p++;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(p,true);
                } else {
                    progressBar.setProgress(p);
                }
                if (flag < questions.size()) {
                    tv.setText(questions.get(flag));
                } else {
                    Intent in = new Intent(getApplicationContext(), ResultActivity.class);
                    in.putExtra("result", correct);
                    in.putExtra("user", Name);
                    in.putExtra("test", intent.getStringExtra("test"));
                    startActivity(in);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                radio_g.clearCheck();
            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BottomLayoutActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}