package com.shashank.mentalhealth.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.mentalhealth.R;

public class optionActivity extends AppCompatActivity {
    Button quiz,music,chatBot, exercise;
    TextView quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        checkRunTimePermission();
        quiz = findViewById(R.id.takeQuiz);
        music = findViewById(R.id.listenMusic);
        quote = findViewById(R.id.quote);
        chatBot = findViewById(R.id.chatBot);
        exercise = findViewById(R.id.exercise);
        quote.setText("Emotional pain is not something that should be hidden away and never spoken about. There is truth in your pain, there is growth in your pain, but only if it’s first brought out into the open.\n" +
                " — Steven Aitchison");
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(optionActivity.this, MentalConditions.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(optionActivity.this, MusicActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        chatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(optionActivity.this, ChatActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(optionActivity.this, ExerciseActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        final AlertDialog.Builder builder = new AlertDialog.Builder(optionActivity.this);
        builder.setMessage("Are you sure you want to leave?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", (dialog, which) -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        builder.setPositiveButton("CANCEL", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newsMenu:
                Intent intent = new Intent(optionActivity.this,NewsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkRunTimePermission(){
         Dexter.withContext(this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
             @Override
             public void onPermissionGranted(PermissionGrantedResponse response) {
             }

             @Override
             public void onPermissionDenied(PermissionDeniedResponse response) {
                 Toast.makeText(optionActivity.this, "Audio Permission is Required for visualizer while listening to music", Toast.LENGTH_SHORT).show();
                 final AlertDialog.Builder builder = new AlertDialog.Builder(optionActivity.this);
                 builder.setMessage("To grant permission press open settings");
                 builder.setCancelable(true);
                 builder.setNegativeButton("CLOSE", (dialog, which) -> {
                     finish();
                     overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 });
                 builder.setPositiveButton("OPEN SETTINGS", (dialog, which) -> {
                     Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                     Uri uri = Uri.fromParts("package", getPackageName(), null);
                     intent.setData(uri);
                     startActivity(intent);
                 });
                 AlertDialog alertDialog = builder.create();
                 alertDialog.show();
             }

             @Override
             public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

             }
         }).check();
     }
}