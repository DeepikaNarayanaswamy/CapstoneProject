package com.deepika.newsnow;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deepika.newsnow.adapter.CustomNewsArrayAdapter;
import com.deepika.newsnow.pojo.News;
import com.deepika.newsnow.provider.NewsContract;
import com.deepika.newsnow.util.NewsNowConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment}subclass.
 */
public class BookmarkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "BookmarkFragment";
    public static int LOADER_ID = 3;
    CustomNewsArrayAdapter mAdapter;
    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        News n = new News();
        ArrayList<News> newsArrayList = new ArrayList<>();
 /*       n.setNewsTitle("Bookmark 1");
        News n1 = new News();
        n1.setNewsTitle("Bookmark 2");
        newsArrayList.add(n);
        newsArrayList.add(n1);
 */       View view = inflater.inflate(R.layout.fragment_bookmark_tab, container, false);
        mAdapter = new CustomNewsArrayAdapter(newsArrayList,this.getContext());
        getLoaderManager().initLoader(LOADER_ID,null,this);

        ListView listView = (ListView)view.findViewById(R.id.bookmarkNewsList);
        listView.setAdapter(mAdapter);

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
        return view;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = NewsContract.News.COLUMN_NAME_IS_BOOKMARKED+ "=?";
        String [] selctionArgs= {"1"};
        String [] projection = {NewsContract.News.COLUMN_NAME_TITLE,NewsContract.News.COLUMN_NAME_DESCRIPTION,NewsContract.News.COLUMN_NAME_IMAGE_LINK,NewsContract.News.COLUMN_NAME_ENTRY_ID};
        CursorLoader cursorLoader = new CursorLoader(getContext(),NewsContract.News.CONTENT_URI,projection,selection,selctionArgs,null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<News>newsList = new ArrayList<>();
        Log.v(TAG,"bnumber of rows ="+data.getCount());
        while(data.moveToNext()){
            News n = new News();
            n.setNewsID(data.getString(data.getColumnIndex(NewsContract.News.COLUMN_NAME_ENTRY_ID)));
            n.setNewsDescription(data.getString(data.getColumnIndex(NewsContract.News.COLUMN_NAME_DESCRIPTION)));
            n.setNewsTitle(data.getString(data.getColumnIndex(NewsContract.News.COLUMN_NAME_TITLE)));
            n.setNewsImageURL(data.getString(data.getColumnIndex(NewsContract.News.COLUMN_NAME_IMAGE_LINK)));
            newsList.add(n);
        }
        mAdapter.setNews(newsList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setNews(new ArrayList<News>());
    }
}
