package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.mentalhealth.Activities.optionActivity;
import com.shashank.mentalhealth.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText nameText, password;
    FirebaseAuth auth;
    AlertDialog dialog;
    boolean login;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // if ui messed up by using translucent navigation 
        nameText = findViewById(R.id.et_name);
        sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        LoadName();
        if (login){
            startActivity(new Intent(MainActivity.this, optionActivity.class));
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        auth = FirebaseAuth.getInstance();
        Button button = findViewById(R.id.btn_start);
        password = findViewById(R.id.et_password);
        TextView reg = findViewById(R.id.reg_tv);
        TextView forgot = findViewById(R.id.forgot);
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress);
        dialog = builder.create();
        //
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email = nameText.getText().toString();
                String pass = password.getText().toString();
                if (validate(email) && pass.length() > 6) {
                    auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            editor.putString("edit", nameText.getText().toString());
                            editor.putBoolean("login", true);
                            editor.apply();
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, optionActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Error logging you in", Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("login", false);
                                    editor.apply();
                                }
                            }, 3000);

                        }
                    });
                } else {
                    if (!validate(email)) {
                        nameText.setError("Enter valid email address");
                    }
                    if (pass.length() < 6) {
                        password.setError("Enter valid password");
                    }
                    dialog.dismiss();
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = nameText.getText().toString();
                dialog.show();
                if (validate(email)) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }, 2000);
                            }
                        }
                    });
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    public void LoadName() {
        nameText.setText(sharedPreferences.getString("edit", ""));
        login = sharedPreferences.getBoolean("login", false);
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}