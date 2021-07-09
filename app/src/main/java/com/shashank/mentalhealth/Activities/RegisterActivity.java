package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.mentalhealth.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText email_reg, pass_1, pass_2;
    FirebaseAuth auth;
    AlertDialog dialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email_reg = findViewById(R.id.et_name_reg);
        pass_1 = findViewById(R.id.et_password_reg);
        pass_2 = findViewById(R.id.et_reenter);
        Button reg = findViewById(R.id.btn_reg);
        auth = FirebaseAuth.getInstance();
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress);
        dialog = builder.create();
        //
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_reg.getText().toString();
                String pass1 = pass_1.getText().toString();
                String pass2 = pass_2.getText().toString();
                if (validate(email) && pass1.length()>6 && pass2.length()>6) {
                    if (pass1.equals(pass2)){
                        dialog.show();
                        register(email,pass1);
                    }
                } else {
                    if (!validate(email)){
                        email_reg.setError("Enter valid email address");
                    }
                    if (pass1.length() < 6){
                        pass_1.setError("Enter valid password");
                    }
                    if (pass2.length() < 6){
                        pass_2.setError("Enter valid password");
                    }
                }
            }
        });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void register(String e, String p){
        auth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("edit",e);
                    editor.apply();
                    Intent intent = new Intent(RegisterActivity.this, optionActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error registering you", Toast.LENGTH_SHORT).show();
                        }
                    },3000);

                }
            }
        });
    }

}