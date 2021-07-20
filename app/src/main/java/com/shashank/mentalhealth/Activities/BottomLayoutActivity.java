package com.shashank.mentalhealth.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.mentalhealth.Fragments.AccountFragment;
import com.shashank.mentalhealth.Fragments.ChatFragment;
import com.shashank.mentalhealth.Fragments.ExerciseFragment;
import com.shashank.mentalhealth.Fragments.MusicFragment;
import com.shashank.mentalhealth.Fragments.QuizFragment;
import com.shashank.mentalhealth.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("deprecation")
public class BottomLayoutActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_layout);
        navigationView = findViewById(R.id.bottom_navigation);
        QuizFragment fragment1 = new QuizFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,  // enter
                        android.R.anim.fade_out,  // exit
                        android.R.anim.fade_in,   // popEnter
                        android.R.anim.slide_out_right  // popExit
                ).replace(R.id.frame, fragment1)
                .commit();
        checkPermission();
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    // Respond to navigation item 1 click
                    if (navigationView.getSelectedItemId() != R.id.page_1) {
                        QuizFragment quizFragment = new QuizFragment();
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        android.R.anim.slide_in_left,  // enter
                                        android.R.anim.fade_out,  // exit
                                        android.R.anim.fade_in,   // popEnter
                                        android.R.anim.slide_out_right  // popExit
                                )
                                .replace(R.id.frame, quizFragment)
                                .commit();
                        return true;
                    } else {
                        return false;
                    }
                case R.id.page_2:
                    if (navigationView.getSelectedItemId() != R.id.page_2) {
                        MusicFragment musicFragment = new MusicFragment();
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        android.R.anim.slide_in_left,  // enter
                                        android.R.anim.fade_out,  // exit
                                        android.R.anim.fade_in,   // popEnter
                                        android.R.anim.slide_out_right  // popExit
                                )
                                .replace(R.id.frame, musicFragment)
                                .commit();
                        // Respond to navigation item 1 click
                        return true;
                    } else {
                        return false;
                    }
                case R.id.page_3:
                    if (navigationView.getSelectedItemId() != R.id.page_3) {
                        ChatFragment fragment11 = new ChatFragment();
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        android.R.anim.slide_in_left,  // enter
                                        android.R.anim.fade_out,  // exit
                                        android.R.anim.fade_in,   // popEnter
                                        android.R.anim.slide_out_right  // popExit
                                )
                                .replace(R.id.frame, fragment11)
                                .commit();
                        // Respond to navigation item 1 click
                        return true;
                    } else {
                        return false;
                    }
                case R.id.page_4:
                    if (navigationView.getSelectedItemId() != R.id.page_4) {
                        ExerciseFragment exerciseFragment = new ExerciseFragment();
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        android.R.anim.slide_in_left,  // enter
                                        android.R.anim.fade_out,  // exit
                                        android.R.anim.fade_in,   // popEnter
                                        android.R.anim.slide_out_right  // popExit
                                )
                                .replace(R.id.frame, exerciseFragment)
                                .commit();
                        // Respond to navigation item 1 click
                        return true;
                    } else {
                        return false;
                    }
                case R.id.page_5:
                    if (navigationView.getSelectedItemId() != R.id.page_5){
                        AccountFragment accountFragment = new AccountFragment();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                android.R.anim.slide_in_left,  // enter
                                android.R.anim.fade_out,  // exit
                                android.R.anim.fade_in,   // popEnter
                                android.R.anim.slide_out_right  // popExit
                        ).replace(R.id.frame, accountFragment).commit();
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newsMenu:
                Intent intent = new Intent(BottomLayoutActivity.this, NewsActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.apply();
                startActivity(new Intent(BottomLayoutActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.page_1 != selectedItemId) {
            setHomeItem(BottomLayoutActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_1);
    }

    private void checkPermission() {
        Dexter.withContext(BottomLayoutActivity.this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(BottomLayoutActivity.this, "Permission Required for Visualizer", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
            }
        }).check();
    }

    public boolean isExists() {
        File f = new File(getExternalFilesDir("F&H").getPath());
        if (!f.exists()) {
            return f.mkdir();
        } else {
            return true;
        }
    }
}