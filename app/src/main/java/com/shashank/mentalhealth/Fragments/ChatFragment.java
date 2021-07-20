package com.shashank.mentalhealth.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.mentalhealth.Adapters.ChatAdapter;
import com.shashank.mentalhealth.BotReply;
import com.shashank.mentalhealth.DB.DBHelper;
import com.shashank.mentalhealth.R;
import com.shashank.mentalhealth.SendMessageInBg;
import com.shashank.mentalhealth.models.Message;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ChatFragment extends Fragment implements BotReply {
    private String TAG = "mainactivity";
    FirebaseAuth auth;
    ImageButton btnSend;
    ChatAdapter chatAdapter;
    RecyclerView chatView;
    DBHelper dbHelper;
    EditText editMessage;
    ArrayList<Message> messageList = new ArrayList<>();
    private BottomNavigationView navigationView;
    private SessionName sessionName;
    private SessionsClient sessionsClient;
    SharedPreferences sharedPreferences;
    private String uuid = UUID.randomUUID().toString();


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("SAGE");
        chatView = rootView.findViewById(R.id.chatView);

        editMessage = rootView.findViewById(R.id.editMessage);
        btnSend = rootView.findViewById(R.id.btnSend);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        FirebaseUser currentUser = auth.getCurrentUser();
        Objects.requireNonNull(currentUser);
        String name = currentUser.getDisplayName();
        String Name;
        if (name != null) {
            Name = name;
        } else {
            Name = sharedPreferences.getString("edit", "Friend").split("@")[0];
        }
//        new Thread(() -> {
//            Gson gson = new Gson();
//            DBHelper dbHelper = new DBHelper(getContext(),null ,1);
//            Type type = new TypeToken<ArrayList<Message>>() {}.getType();
//            messageList =  gson.fromJson(dbHelper.fetchChat(Name), type);
//            if (messageList == null) {
//                messageList = new ArrayList<>();
//            }
//            requireActivity().runOnUiThread(() -> {
//                chatAdapter = new ChatAdapter(getContext(), messageList, getActivity(), Name);
//                chatView.setAdapter(chatAdapter);
//            });
//        }).start();
        chatView.animate().alpha(0.0f);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                chatView.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        DBHelper dbHelper = new DBHelper(getContext(), null, 1);
                        Type type = new TypeToken<ArrayList<Message>>() {
                        }.getType();
                        messageList = gson.fromJson(dbHelper.fetchChat(Name), type);
                        if (messageList == null) {
                            messageList = new ArrayList<>();
                        }
                        chatAdapter = new ChatAdapter(getContext(), messageList, getActivity(), Name);
                        chatView.setAdapter(chatAdapter);
                        chatView.animate().alpha(1.0f);
                        chatView.smoothScrollToPosition(chatAdapter.getItemCount());
                    }
                });
            }
        }, 1000);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));
                    editMessage.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot();
        return rootView;
    }

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}