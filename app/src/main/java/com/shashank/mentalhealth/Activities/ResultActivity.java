package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.shashank.mentalhealth.DB.DBHelper;
import com.shashank.mentalhealth.R;

import java.io.ByteArrayOutputStream;

public class ResultActivity extends AppCompatActivity {
    TextView tv, tv2, tv3;
    Button Exit, reset;
    int results;
    DBHelper db;
    GraphView graphView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        results = intent.getIntExtra("result", 0);
        String testName = intent.getStringExtra("test");
        String UserName = intent.getStringExtra("user");
        tv = findViewById(R.id.resultsAndnotes);
        tv2 = findViewById(R.id.score);
        tv3 = findViewById(R.id.suggestions);
        Exit = findViewById(R.id.btnExit);
        reset = findViewById(R.id.resetGraph);
        graphView = findViewById(R.id.graphResult);
        //
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        db = new DBHelper(this,null ,1);
        db.insertResult(testName,UserName, results);
        db.showResults(testName, UserName, graphView, series);
        //
        tv2.setText(tv2.getText() + ": " + results);
        if (results <= 5) {
            switch (testName){
                case "DEPRESSION":
                    tv.setText("Minimal depression");
                    tv3.setText("Suggestions:\n" +
                            "1. Get in a routine.\n" +
                            "2. Set goals.\n" +
                            "3. Exercise.");
                    break;
                case "BIPOLAR":
                    tv.setText("Minimal Bipolar symptoms");
                    tv3.setText("Suggestions:\n" +
                            "1. Don’t be afraid\n" +
                            "2. Stay active");
                    break;
                case "ANXIETY":
                    tv3.setText("Suggestions:\n"+
                            "1. Stay active\n" +
                            "2. Don’t drink alcohol");
                    break;
                default:
                    tv3.setText("Error!!");
            }
        } else if (results <= 10) {
            switch (testName){
                case "DEPRESSION":
                    tv.setText("Mild depression");
                    tv3.setText("Suggestions:\n"+
                            "1. Eat healthy.\n" +
                            "2. Get enough sleep.\n" +
                            "3. Take on responsibilities.");
                    break;
                case "BIPOLAR":
                    tv.setText("Mild Bipolar symptoms");
                    tv3.setText("Suggestions:\n"+
                            "1. Stick to a healthy routine\n" +
                            "2. Structure your day");
                    break;
                case "ANXIETY":
                    tv.setText("Mild Anxiety");
                    tv3.setText("Suggestions:\n"+
                            "1. Stop smoking\n" +
                            "2. Ditch caffeine");
                    break;
                default:
                    tv3.setText("Error!!");
            }
        } else if (results <= 14) {
            switch (testName){
                case "DEPRESSION":
                    tv.setText("Moderate depression");
                    tv3.setText("Suggestions:\n" +
                            "1. Challenge negative thoughts.\n" +
                            "2. Check with your doctor before using supplements.\n" +
                            "3. Do something new.");
                    break;
                case "BIPOLAR":
                    tv.setText("Moderate Bipolar symptoms");
                    tv3.setText("Suggestions:\n"+
                            "1. Don’t isolate yourself\n" +
                            "2. Find new ways to relieve stress");
                    break;
                case "ANXIETY":
                    tv.setText("Moderate Anxiety");
                    tv3.setText("Suggestions:\n"+
                            "1. Get some sleep\n" +
                            "2. Meditate");
                    break;
                default:
                    tv3.setText("Error!!");
            }
        } else if (results <= 20) {
            switch (testName){
                case "DEPRESSION":
                    tv.setText("Moderately severe depression");
                    tv3.setText("Suggestions:\n" +
                            "1. Try to have fun. \n" +
                            "2. Meaning: Find small ways to be of service to others. \n" +
                            "3. Your goals: Find workable goals that give you a sense of accomplishment.");
                    break;
                case "BIPOLAR":
                    tv.setText("Moderately severe bipolar symptoms");
                    tv3.setText("Suggestions:\n"+
                            "1. Join a support group \n" +
                            "2. medication\n");
                    break;
                case "ANXIETY":
                    tv.setText("Moderately severe anxiety");
                    tv3.setText("Suggestions:\n"+
                            "1. Eat a healthy diet\n" +
                            "2. Practice deep breathing");
                    break;
                default:
                    tv3.setText("Error!!");
            }
        } else {
            switch (testName){
                case "DEPRESSION":
                    tv.setText("Severe depression");
                    tv3.setText("Suggestions:\n"+
                            "1. Pleasant Events: Schedule pleasant activities or events.\n" +
                            "2. Engagement: Stay in the present.\n" +
                            "3. Exercise: And, eat right too.");
                    break;
                case "BIPOLAR":
                    tv.setText("Severe bipolar symptoms");
                    tv3.setText("Suggestions:\n"+
                            "1. physical intervention\n" +
                            "2. lifestyle remedies");
                    break;
                case "ANXIETY":
                    tv.setText("Severe Anxiety");
                    tv3.setText("Suggestions:\n" +
                            "1. Try aromatherapy\n" +
                            "2. Drink chamomile tea");
                    break;
                default:
                    tv3.setText("Error!!");
            }
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphView.removeAllSeries();
                db.deleteResult(testName, UserName);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), optionActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
//                graphView.takeSnapshotAndShare(ResultActivity.this,"myGraph result","");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Bitmap inImage = graphView.takeSnapshot();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                String path = MediaStore.Images.Media.insertImage(ResultActivity.this.getContentResolver(), inImage, "",null);
                if (path == null) {
                    // most likely a security problem
                    throw new SecurityException("Could not get path from MediaStore. Please check permissions.");
                }
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                i.putExtra(Intent.EXTRA_TEXT, "My Current Mental health score is "+ results + " on Feeling & Healing.");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setType("image/*");
                startActivity(Intent.createChooser(i, "send"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}