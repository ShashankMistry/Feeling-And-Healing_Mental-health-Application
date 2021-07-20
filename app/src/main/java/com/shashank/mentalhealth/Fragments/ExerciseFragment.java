package com.shashank.mentalhealth.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.shashank.mentalhealth.Dialog.ExerciseDialog;
import com.shashank.mentalhealth.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExerciseFragment extends Fragment {
    private String currentDate, exe1, exe2,exe3;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView listView;
    private Date now;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Exercise");
//        first = rootView.findViewById(R.id.exercise1);
//        second = rootView.findViewById(R.id.exercise2);
//        third = rootView.findViewById(R.id.exercise3);
        String[] exercise = {"Go for 30 minute walk","Mediate for 10 minutes", "Play any outdoor game"};
        listView = rootView.findViewById(R.id.suggestions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.checked_text_view,R.id.CTV,exercise);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        //
        exe1 = "";
        exe2 = "";
        exe3 = "";
        //
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        //
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (!listView.isItemChecked(position)) {
                editor.putString(String.valueOf(position),"");
                editor.apply();
            } else {
                @SuppressLint("SimpleDateFormat") String exe = new SimpleDateFormat("dd-MM-yyyy").format(now);
                editor.putString(String.valueOf(position), exe);
                editor.apply();
                Toast.makeText(getContext(), "WOW nice, keep it up", Toast.LENGTH_LONG).show();
                ExerciseDialog exerciseDialog = new ExerciseDialog();
                exerciseDialog.show(getChildFragmentManager(),"exercise");
                new Handler(Looper.getMainLooper()).postDelayed(exerciseDialog::dismiss,3000);
            }
        });
        //
        return rootView;
    }

    private void loadPrefs() {
        currentDate = sharedPreferences.getString("current","");
        exe1 = sharedPreferences.getString("0", "");
        exe2 = sharedPreferences.getString("1","");
        exe3 = sharedPreferences.getString("2","");
        if (currentDate.equals(exe1) || currentDate.equals(exe2) || currentDate.equals(exe3)) {
            if (currentDate.equals(exe1)) {
                listView.setItemChecked(0,true);
            }
            if (currentDate.equals(exe2)) {
                listView.setItemChecked(1,true);
            }
            if (currentDate.equals(exe3)) {
                listView.setItemChecked(2,true);
            }
        } else{
            listView.setItemChecked(0,false);
            listView.setItemChecked(1,false);
            listView.setItemChecked(2,false);
        }
    }

    @SuppressLint("SimpleDateFormat")
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