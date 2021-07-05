package com.shashank.mentalhealth.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.mentalhealth.Activities.ExerciseActivity;
import com.shashank.mentalhealth.models.Message;
import com.shashank.mentalhealth.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Message> messageList;
    private Activity activity;
    private Context mContext;

    public ChatAdapter(Context context, List<Message> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
        mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_message_one, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String message = messageList.get(position).getMessage();
        boolean isReceived = messageList.get(position).getIsReceived();
        if (isReceived) {
            holder.messageReceive.setVisibility(View.VISIBLE);
            holder.messageSend.setVisibility(View.GONE);
            if (message.contains("exercise")) {
                holder.messageReceive.setText(message + "\n\n" + "I'll redirect you to exercise page");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, ExerciseActivity.class);
                        mContext.startActivity(intent);
                        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                }, 5000);

            } else {
                holder.messageReceive.setText(message);
            }
        } else {
            holder.messageSend.setVisibility(View.VISIBLE);
            holder.messageReceive.setVisibility(View.GONE);
            holder.messageSend.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView messageSend;
        TextView messageReceive;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSend = itemView.findViewById(R.id.message_send);
            messageReceive = itemView.findViewById(R.id.message_receive);
        }
    }

}
