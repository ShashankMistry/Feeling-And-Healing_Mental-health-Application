package com.shashank.mentalhealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shashank.mentalhealth.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 22;
    SharedPreferences sharedPreferences;
    EditText nameText, password;
    FirebaseAuth auth;
    AlertDialog dialog;
    private GoogleSignInClient mGoogleSignInClient;
    //    boolean login;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // if ui messed up by using translucent navigation 
        nameText = findViewById(R.id.et_email);
        TextInputLayout textInputLayoutEmail = findViewById(R.id.input_email);
        TextInputLayout textInputLayoutPass = findViewById(R.id.input_pass);
        sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        LoadName();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, BottomLayoutActivity.class));
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        Button button = findViewById(R.id.btn_start);
        Button google = findViewById(R.id.google);
        password = findViewById(R.id.et_password);
        TextView reg = findViewById(R.id.reg_tv);
        TextView forgot = findViewById(R.id.forgot);
        TextView error = findViewById(R.id.error);
        error.setVisibility(View.GONE);
        error.setText("email id or password is incorrect");
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
                            editor.putString("pass1092",password.getText().toString());
                            editor.putBoolean("google", false);
//                            editor.putBoolean("login", true);
                            editor.apply();
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, BottomLayoutActivity.class);
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
                                    error.setVisibility(View.VISIBLE);
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            error.setVisibility(View.GONE);
                                        }
                                    }, 2000);
                                }
                            }, 3000);

                        }
                    });
                } else {
                    if (!validate(email)) {
                        textInputLayoutEmail.setError("Enter valid email address");
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textInputLayoutEmail.setError(null);
                            }
                        },1500);
                    }
                    if (pass.length() < 6) {
                        textInputLayoutPass.setError("Password must be greater than 6 characters");
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textInputLayoutPass.setError(null);
                            }
                        },1500);
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

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                editor.putBoolean("google",true);
                editor.apply();
                signIn();
            }
        });
    }

    public void LoadName() {
        nameText.setText(sharedPreferences.getString("edit", ""));
//        login = sharedPreferences.getBoolean("login", false);
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
                dialog.show();
            } catch (Exception e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(MainActivity.this, BottomLayoutActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Error registering you", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}