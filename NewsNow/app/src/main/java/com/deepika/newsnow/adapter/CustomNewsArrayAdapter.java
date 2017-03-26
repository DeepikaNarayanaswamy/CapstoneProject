package com.deepika.newsnow.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepika.newsnow.R;
import com.deepika.newsnow.pojo.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/25/2017.
 */

public class CustomNewsArrayAdapter extends ArrayAdapter<News> {
    ArrayList<News> data;
    Context context;
    private static class ViewHolder {
        TextView newsTitle;
        ImageView newsImage;

    }
    public CustomNewsArrayAdapter(ArrayList<News> newsArrayList, Context ctx){
        super(ctx, R.layout.newslist,newsArrayList);
        data = newsArrayList;
        context = ctx;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = data.get(position);

        CustomNewsArrayAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomNewsArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.newslist, parent, false);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomNewsArrayAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
        // TODO set image

        viewHolder.newsTitle.setText(news.getNewsTitle());
        return convertView;

    }

    public void setNews(List<News> dataList) {
        data.addAll(dataList);
        notifyDataSetChanged();
    }
}
