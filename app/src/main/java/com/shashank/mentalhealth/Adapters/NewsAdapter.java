package com.shashank.mentalhealth.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.mentalhealth.R;
import com.shashank.mentalhealth.models.news;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<news> {
    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<news> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView newsDes;
        TextView newsTitle;
        RoundedImageView newsImg;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Des = getItem(position).getDes();
        String Img = getItem(position).getImgUrl();
        String Title = getItem(position).getTitle();
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_news, parent, false);
            holder = new NewsAdapter.ViewHolder();
            holder.newsDes = convertView.findViewById(R.id.newsDes);
            holder.newsImg = convertView.findViewById(R.id.newsImg);
            holder.newsTitle = convertView.findViewById(R.id.newsTitle);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.newsTitle.setText(Title);
        holder.newsDes.setText(Des);
        if (Des.equals("null")){
            holder.newsDes.setText("");
            holder.newsTitle.setText(Title);
        }
        Glide.with(getContext()).load(Img).placeholder(R.drawable.ispinner).into(holder.newsImg);
        //placeholder set declared image when no image is available from url
        return convertView;
    }
}
