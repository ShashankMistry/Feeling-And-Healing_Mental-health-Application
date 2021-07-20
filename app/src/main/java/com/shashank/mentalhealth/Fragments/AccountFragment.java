package com.shashank.mentalhealth.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.mentalhealth.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;


@SuppressWarnings("deprecation")
public class AccountFragment extends Fragment {

    private static final String DATABASE_NAME = "GraphData.DB";
    private static final int IMPORT_DB = 1232;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Toast.makeText(this, resultCode+"", Toast.LENGTH_SHORT).show();
        if (resultCode == -1) {
            switch (requestCode) {
                case BACKUP_CODE:
                    try {
                        assert data != null;
                        FileOutputStream stream = (FileOutputStream) requireActivity().getContentResolver().openOutputStream(data.getData());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Files.copy(Paths.get(requireActivity().getDatabasePath(DATABASE_NAME).getPath()), stream);
                        }
                        Toast.makeText(getContext(), "exported", Toast.LENGTH_SHORT).show();
                        stream.close(); ///very important
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error occurred in backup", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case IMPORT_DB:
                    try {
                        File file = new File(requireActivity().getDatabasePath(DATABASE_NAME).getPath());
                        assert data != null;
                        FileInputStream stream = (FileInputStream) requireActivity().getContentResolver().openInputStream(data.getData());
                        FileChannel channel = stream.getChannel();
                        FileChannel channel3 = new FileOutputStream(file).getChannel();
                        channel3.transferFrom(channel, 0, channel.size());
                        Toast.makeText(getContext(), "imported", Toast.LENGTH_SHORT).show();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private static final int BACKUP_CODE = 123;

    public AccountFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        TextView initial = rootView.findViewById(R.id.initial);
        TextView name_acc = rootView.findViewById(R.id.name_account);
        TextView Email = rootView.findViewById(R.id.email_account);
        TextView error  = rootView.findViewById(R.id.error_acc);
        Button change = rootView.findViewById(R.id.password_change);
        Button export = rootView.findViewById(R.id.exp_acc);
        Button imp = rootView.findViewById(R.id.imp_acc);
        TextInputEditText password = rootView.findViewById(R.id.et_password_acc);
        TextInputEditText confirm = rootView.findViewById(R.id.et_reenter_acc);
//        TableLayout p1 = rootView.findViewById(R.id.input_pass_acc);
//        TableLayout p2 = rootView.findViewById(R.id.input_reenter_acc);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String withAt = user.getEmail();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("EditText", Context.MODE_PRIVATE);
        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        if (name != null) {
            name_acc.setText("Name: " + name);
            initial.setText(name.charAt(0) + "");
        } else {
            assert withAt != null;
            String[] email = withAt.split("@");
            name_acc.setText("Name: " + email[0]);
            initial.setText(email[0].charAt(0) + "");
        }
        Email.setText("Email: " + withAt);
        change.setEnabled(false);
        String passOld = sharedPreferences.getString("pass1092", "");
        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passOld.equals(password.getText().toString()) && confirm.getText().toString().length() > 5) {
                    change.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.updatePassword(confirm.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (sharedPreferences.getBoolean("google", true)){
            password.setEnabled(false);
            confirm.setEnabled(false);
            error.setText("You signed in with google, password can't be changed");
        } else {
            Toast.makeText(getContext(), sharedPreferences.getBoolean("google",true)+"", Toast.LENGTH_SHORT).show();
            password.setEnabled(true);
            confirm.setEnabled(true);
            error.setText("password must contain 6 characters");
        }

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.setType("application/octet-stream"); // application/octet-stream for handling database files
                i.putExtra(Intent.EXTRA_TITLE, DATABASE_NAME);
                startActivityForResult(i, BACKUP_CODE);
            }
        });

        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("application/octet-stream");
                startActivityForResult(intent1, IMPORT_DB);
            }
        });

        return rootView;
    }
}