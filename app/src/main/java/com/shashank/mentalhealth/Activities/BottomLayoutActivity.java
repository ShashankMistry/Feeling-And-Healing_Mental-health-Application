package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.mentalhealth.Fragments.ChatFragment;
import com.shashank.mentalhealth.Fragments.ExerciseFragment;
import com.shashank.mentalhealth.Fragments.MusicFragment;
import com.shashank.mentalhealth.Fragments.QuizFragment;
import com.shashank.mentalhealth.R;

import org.jetbrains.annotations.NotNull;

public class BottomLayoutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_layout);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        QuizFragment fragment1 = new QuizFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,  // enter
                        android.R.anim.fade_out,  // exit
                        android.R.anim.fade_in,   // popEnter
                        android.R.anim.slide_out_right  // popExit
                ).replace(R.id.frame, fragment1)
                .commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
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
                                    .addToBackStack(null)
                                    .commit();
                            // Respond to navigation item 1 click
                            return true;
                        } else {
                            return false;
                        }
                    case R.id.page_3:
                        if (navigationView.getSelectedItemId() != R.id.page_3) {
                            ChatFragment fragment1 = new ChatFragment(navigationView);
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            android.R.anim.slide_in_left,  // enter
                                            android.R.anim.fade_out,  // exit
                                            android.R.anim.fade_in,   // popEnter
                                            android.R.anim.slide_out_right  // popExit
                                    )
                                    .replace(R.id.frame, fragment1)
                                    .addToBackStack(null)
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
                                    .addToBackStack(null)
                                    .commit();
                            // Respond to navigation item 1 click
                            return true;
                        } else {
                            return false;
                        }
                    default:
                        return false;
                }
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }
}