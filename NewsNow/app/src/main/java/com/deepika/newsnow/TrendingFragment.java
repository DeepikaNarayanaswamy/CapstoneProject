package com.deepika.newsnow;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepika.newsnow.adapter.CustomNewsArrayAdapter;
import com.deepika.newsnow.pojo.News;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        News n = new News();
        ArrayList<News> newsArrayList = new ArrayList<>();
        n.setNewsTitle("Trending 1");
        News n1 = new News();
        n1.setNewsTitle("Trending 2");
        newsArrayList.add(n);
        newsArrayList.add(n1);
        View view = inflater.inflate(R.layout.fragment_trending_tab, container, false);
        CustomNewsArrayAdapter adapter = new CustomNewsArrayAdapter(newsArrayList,this.getContext());


        ListView listView = (ListView)view.findViewById(R.id.trendingNewsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent NewsDetailIntent = new Intent(view.getContext(),NewsDetail.class);
                // Here we need to pass the news Id to get the full details
                startActivity(NewsDetailIntent);
            }
        });
        return view;

    }

}
