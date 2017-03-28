package com.deepika.newsnow;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepika.newsnow.adapter.CustomNewsArrayAdapter;
import com.deepika.newsnow.pojo.News;
import com.deepika.newsnow.provider.NewsContract;
import com.deepika.newsnow.sync.NewsAsyncTaskLoader;
import com.deepika.newsnow.util.NewsNowConstants;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadlinesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String TAG = "HeadlinesFragment";
    private static final String[] PROJECTION = new String[]{
            NewsContract.News._ID,
            NewsContract.News.COLUMN_NAME_TITLE,
            NewsContract.News.COLUMN_NAME_IMAGE_LINK,

    };
    private static final String[] FROM_COLUMNS = new String[]{
            NewsContract.News.COLUMN_NAME_TITLE,
            NewsContract.News.COLUMN_NAME_IMAGE_LINK
    };

    /**
     * List of Views which will be populated by Cursor data.
     */
    private static final int[] TO_FIELDS = new int[]{
            android.R.id.text1,
            android.R.id.text2};
    private CustomNewsArrayAdapter mAdapter;
    private Object mSyncObserverHandle;
    private static int LOADER_ID = 1;
    public HeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        News n = new News();
        ArrayList<News>newsArrayList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
        mAdapter = new CustomNewsArrayAdapter(newsArrayList,this.getContext());
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        ListView listView = (ListView)view.findViewById(R.id.list);
        listView.setAdapter(mAdapter);
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

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsAsyncTaskLoader(HeadlinesFragment.this.getActivity(), NewsNowConstants.NEWS_SORTBY_TOP);
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
