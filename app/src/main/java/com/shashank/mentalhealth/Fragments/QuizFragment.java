package com.shashank.mentalhealth.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shashank.mentalhealth.Activities.QuizActivity;
import com.shashank.mentalhealth.DB.DBHelper;
import com.shashank.mentalhealth.R;

public class QuizFragment extends Fragment {
    Button depression,bipolar,anxiety;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_conditions, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Quiz");
        depression = rootView.findViewById(R.id.dep);
        bipolar = rootView.findViewById(R.id.bip);
        anxiety = rootView.findViewById(R.id.anx);
       //
        DBHelper dbHelper = new DBHelper(getContext(),null, 1);
//        try {
//            dbHelper.defaultQuestions();
//        }catch (Exception e){
//            Log.w("myApp", "already has data");
//        }

        depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent depressionIntent = new Intent(getContext(), QuizActivity.class);
                depressionIntent.putExtra("test","DEPRESSION");
                startActivity(depressionIntent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        bipolar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent bipolarIntent = new Intent(getContext(), QuizActivity.class);
                bipolarIntent.putExtra("test","BIPOLAR");
                startActivity(bipolarIntent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxietyIntent = new Intent(getContext(), QuizActivity.class);
                anxietyIntent.putExtra("test","ANXIETY");
                startActivity(anxietyIntent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        return rootView;
    }
}