package com.shashank.mentalhealth.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.mentalhealth.Activities.optionActivity;
import com.shashank.mentalhealth.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Button startbutton = findViewById(R.id.btn_start);
        nameText = findViewById(R.id.et_name);
        sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        LoadName();
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("edit", nameText.getText().toString());
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), optionActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    public void LoadName() {
        nameText.setText(sharedPreferences.getString("edit",""));
    }
}