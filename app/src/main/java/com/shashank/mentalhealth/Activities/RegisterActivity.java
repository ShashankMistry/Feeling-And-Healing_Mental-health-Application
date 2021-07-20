package com.shashank.mentalhealth.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shashank.mentalhealth.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 23;
    EditText email_reg, pass_1, pass_2;
    FirebaseAuth auth;
    TextInputLayout textInputLayoutEmail, textInputLayoutPass, textInputLayoutRePass;
    private GoogleSignInClient mGoogleSignInClient;
    AlertDialog dialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        //
        email_reg = findViewById(R.id.et_email_reg);
        pass_1 = findViewById(R.id.et_password_reg);
        pass_2 = findViewById(R.id.et_reenter);
        textInputLayoutEmail = findViewById(R.id.input_email_reg);
        textInputLayoutPass = findViewById(R.id.input_pass_reg);
        textInputLayoutRePass = findViewById(R.id.input_reenter);
        sharedPreferences = getSharedPreferences("EditText", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Button reg = findViewById(R.id.btn_reg);
        Button google_reg = findViewById(R.id.google_reg);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
        mGoogleSignInClient.signOut();
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress);
        dialog = builder.create();
        //
        reg.setOnClickListener(v -> {
            String email = email_reg.getText().toString();
            String pass1 = pass_1.getText().toString();
            String pass2 = pass_2.getText().toString();
            if (validate(email) && pass1.length() > 5 && pass2.length() > 5) {
                if (pass1.equals(pass2)) {
                    dialog.show();
                    register(email, pass1);
                } else {
                    textInputLayoutPass.setError("passwords do not match");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> textInputLayoutPass.setError(null),2000);

                }
            } else {
                if (!validate(email)) {
                    textInputLayoutEmail.setError("Enter valid email address");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> textInputLayoutEmail.setError(null),2000);
                }
                if (pass1.length() < 6) {
                    textInputLayoutPass.setError("password must be more than 6 letters");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> textInputLayoutPass.setError(null),2000);
                }
                if (pass2.length() < 6) {
                    textInputLayoutRePass.setError("password must be more than 6 letters");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> textInputLayoutRePass.setError(null),2000);
                }
            }
        });
        google_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("google", true);
                editor.apply();
                signIn();
            }
        });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void register(String e, String p) {
        auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()) {
                dialog.dismiss();
                editor.putString("edit", e);
                editor.putString("pass1092", pass_1.getText().toString());
                editor.putBoolean("google", false);
                editor.apply();
                Intent intent = new Intent(RegisterActivity.this, BottomLayoutActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                }, 3000);

            }
        });
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
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent intent = new Intent(RegisterActivity.this, BottomLayoutActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterActivity.this, "Error registering you", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}