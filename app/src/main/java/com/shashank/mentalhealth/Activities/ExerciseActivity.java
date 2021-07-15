package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.shashank.mentalhealth.Dialog.ExerciseDialog;
import com.shashank.mentalhealth.R;

import java.text.SimpleDateFormat;
import java.util.Date;
@SuppressLint("SimpleDateFormat")
public class ExerciseActivity extends AppCompatActivity {
    private String currentDate, exe1, exe2,exe3;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckedTextView first, second, third;
    private Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        //
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        //
        first = findViewById(R.id.exercise1);
        second = findViewById(R.id.exercise2);
        third = findViewById(R.id.exercise3);
        //
        exe1 = "";
        exe2 = "";
        exe3 = "";
        //
        sharedPreferences = getSharedPreferences("exercise",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first.isChecked()){
                    first.setChecked(false);
                    editor.putString("exe1", "");
                    editor.apply();
                }else{
                    Toast.makeText(ExerciseActivity.this, "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe1 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe1", exe1);
                    editor.apply();
                    first.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getSupportFragmentManager(),"exercise");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           exerciseDialog.dismiss();
                        }
                    },3000);

                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second.isChecked()){
                    second.setChecked(false);
                    editor.putString("exe2", "");
                    editor.apply();
                }else{
                    Toast.makeText(ExerciseActivity.this, "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe2 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe2", exe2);
                    editor.apply();
                    second.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getSupportFragmentManager(),"exercise");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exerciseDialog.dismiss();
                        }
                    },3000);
                }
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (third.isChecked()){
                    third.setChecked(false);
                    editor.putString("exe3", "");
                    editor.apply();
                }else{
                    Toast.makeText(ExerciseActivity.this, "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe3 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe3", exe3);
                    editor.apply();
                    third.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getSupportFragmentManager(),"exercise");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exerciseDialog.dismiss();
                        }
                    },3000);
                }
            }
        });
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void loadPrefs() {
        currentDate = sharedPreferences.getString("current","");
        exe1 = sharedPreferences.getString("exe1", "");
        exe2 = sharedPreferences.getString("exe2","");
        exe3 = sharedPreferences.getString("exe3","");
        if (currentDate.equals(exe1) || currentDate.equals(exe2) || currentDate.equals(exe3)) {
            if (currentDate.equals(exe1)) {
                first.setChecked(true);
            }
            if (currentDate.equals(exe2)) {
                second.setChecked(true);
            }
            if (currentDate.equals(exe3)) {
                third.setChecked(true);
            }
        } else{
            first.setChecked(false);
            second.setChecked(false);
            third.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        now = new Date();
        currentDate = new SimpleDateFormat("dd-MM-yyyy").format(now);
//        Toast.makeText(this,currentDate+"", Toast.LENGTH_SHORT).show();
        editor.putString("current", currentDate);
        editor.apply();
        loadPrefs();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}