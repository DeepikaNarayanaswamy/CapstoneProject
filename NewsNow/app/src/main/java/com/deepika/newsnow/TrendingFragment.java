package com.deepika.newsnow;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepika.newsnow.adapter.CustomNewsArrayAdapter;
import com.deepika.newsnow.pojo.News;
import com.deepika.newsnow.sync.NewsAsyncTaskLoader;
import com.deepika.newsnow.util.NewsNowConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String TAG = "TrendingFragment";
    private CustomNewsArrayAdapter mAdapter;
    private static int LOADER_ID = 2;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private LoaderManager.LoaderCallbacks<List<News>> callbacks;
    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        News n = new News();
        ArrayList<News> newsArrayList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_trending_tab, container, false);
        mAdapter = new CustomNewsArrayAdapter(newsArrayList,this.getContext());
        callbacks  = this;
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        ListView listView = (ListView)view.findViewById(R.id.trendingNewsList);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent NewsDetailIntent = new Intent(view.getContext(),NewsDetail.class);
                // Here we need to pass the news Id to get the full details
                News n = mAdapter.getItem(i);
                n.getNewsDescription();
                NewsDetailIntent.putExtra(NewsNowConstants.DESCRIPTION,n.getNewsDescription());
                NewsDetailIntent.putExtra(NewsNowConstants.TITLE,n.getNewsTitle());
                NewsDetailIntent.putExtra(NewsNowConstants.NEWSIMAGEURL,n.getNewsImageURL());
                NewsDetailIntent.putExtra(NewsNowConstants.NEWSURL,n.getNewsURL());
                NewsDetailIntent.putExtra(NewsNowConstants.NEWS_ID,n.getNewsID());
                startActivity(NewsDetailIntent);
            }
        });



//listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    Log.v(TAG,"preference chagnes");
                    getLoaderManager().restartLoader(LOADER_ID, null, callbacks);

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);



        return view;

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsAsyncTaskLoader(TrendingFragment.this.getActivity(), NewsNowConstants.NEWS_SORTBY_POPULAR);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        Log.v(TAG,"onLoadFinished");
        mAdapter.setNews(data);
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.setNews(new ArrayList<News>());
    }


}
