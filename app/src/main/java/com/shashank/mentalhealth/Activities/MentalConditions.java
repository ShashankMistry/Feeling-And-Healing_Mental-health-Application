package com.shashank.mentalhealth.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shashank.mentalhealth.DB.DBHelper;
import com.shashank.mentalhealth.R;

public class MentalConditions extends AppCompatActivity {
    Button depression,bipolar,anxiety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        depression = findViewById(R.id.dep);
        bipolar = findViewById(R.id.bip);
        anxiety = findViewById(R.id.anx);
        DBHelper dbHelper = new DBHelper(this,null, 1);
        try {
            dbHelper.defaultQuestions();
        }catch (Exception e){
            Log.w("myApp", "already has data");
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent depressionIntent = new Intent(MentalConditions.this, QuizActivity.class);
                depressionIntent.putExtra("test","DEPRESSION");
                startActivity(depressionIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        bipolar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent bipolarIntent = new Intent(MentalConditions.this, QuizActivity.class);
                bipolarIntent.putExtra("test","BIPOLAR");
                startActivity(bipolarIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
;        });

        anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxietyIntent = new Intent(MentalConditions.this, QuizActivity.class);
                anxietyIntent.putExtra("test","ANXIETY");
                startActivity(anxietyIntent);
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