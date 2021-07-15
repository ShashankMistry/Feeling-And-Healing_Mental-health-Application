package com.shashank.mentalhealth.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.shashank.mentalhealth.Activities.ExerciseActivity;
import com.shashank.mentalhealth.Dialog.ExerciseDialog;
import com.shashank.mentalhealth.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExerciseFragment extends Fragment {
    private String currentDate, exe1, exe2,exe3;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckedTextView first, second, third;
    private Date now;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_exercise, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Exercise");
        first = rootView.findViewById(R.id.exercise1);
        second = rootView.findViewById(R.id.exercise2);
        third = rootView.findViewById(R.id.exercise3);
        //
        exe1 = "";
        exe2 = "";
        exe3 = "";
        //
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
                    Toast.makeText(getContext(), "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe1 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe1", exe1);
                    editor.apply();
                    first.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getChildFragmentManager(),"exercise");
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
                    Toast.makeText(getContext(), "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe2 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe2", exe2);
                    editor.apply();
                    second.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getChildFragmentManager(),"exercise");
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
                    Toast.makeText(getContext(), "WOW nice, keep it up", Toast.LENGTH_SHORT).show();
                    exe3 = new SimpleDateFormat("dd-MM-yyyy").format(now);
                    editor.putString("exe3", exe3);
                    editor.apply();
                    third.setChecked(true);
                    ExerciseDialog exerciseDialog = new ExerciseDialog();
                    exerciseDialog.show(getChildFragmentManager(),"exercise");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exerciseDialog.dismiss();
                        }
                    },3000);
                }
            }
        });
        return rootView;
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
    public void onResume() {
        super.onResume();
        now = new Date();
        currentDate = new SimpleDateFormat("dd-MM-yyyy").format(now);
//        Toast.makeText(this,currentDate+"", Toast.LENGTH_SHORT).show();
        editor.putString("current", currentDate);
        editor.apply();
        loadPrefs();
    }
}