package com.deepika.newsnow.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.deepika.newsnow.pojo.News;
import com.deepika.newsnow.provider.NewsContract;
import com.deepika.newsnow.util.NewsNowConstants;
import com.deepika.newsnow.util.NewsNowUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/26/2017.
 */

public class NewsAsyncTaskLoader extends AsyncTaskLoader<List<News>>{
    public static final String TAG = "NewsAsyncTaskLoader";
    private final ContentResolver mContentResolver;
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds
    private String newsType;
    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    public NewsAsyncTaskLoader(Context context, String newsTYPE){
        super(context);
        mContentResolver = context.getContentResolver();
        newsType = newsTYPE;
    }

    @Override
    public List<News> loadInBackground() {

        Log.v("sync adapter", "sync adapter called");
        Log.v(TAG,newsType);
        List<News> newsList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String category = preferences.getString("news_category_list", null);
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder(NewsNowConstants.NEWSORGURL_ARTICLES);

        stringBuilder.append(NewsNowConstants.QUESTION_MARK);


        if (category!=null){
            switch (category) {
                case NewsNowConstants.NEWS_CATEGORY_GENERAL:
                    stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "=" + NewsNowConstants.THE_HINDU);
                    break;
                case NewsNowConstants.NEWS_CATEGORY_SPORTS:
                    stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "=" + NewsNowConstants.ESPN);
                    break;

                case NewsNowConstants.NEWS_CATEGORY_WILDLIFE:
                    stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "=" + NewsNowConstants.NATIONAL_GEOGRAPHIC);
                    break;
                case NewsNowConstants.NEWS_CATEGORY_TECHNOLOGY:
                    stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "=" + NewsNowConstants.TECH_CRUNCH);
                    break;
                default:
                    stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "="  + NewsNowConstants.THE_HINDU);
                    break;
            }
        }else{
            stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + "=" + NewsNowConstants.THE_HINDU);
        }

        stringBuilder.append(NewsNowConstants.AND + NewsNowConstants.NEWS_SORTBY_PARAMETER + "=" + newsType);
        stringBuilder.append(NewsNowConstants.AND + NewsNowConstants.NEWS_APIKEY_PARAMETER + "=" + NewsNowConstants.NEWS_APIKEY_VALUE);
        try {
            URL url = new URL(stringBuilder.toString());
            InputStream stream = downloadUrl(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer != null)
            {
                newsList = NewsNowUtil.parseJSONString(buffer.toString());
                insertIntoTable(newsList);
                Log.v(TAG, buffer.toString());
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Here we have to insert in the db.

        return newsList;
    }
    private InputStream downloadUrl(final URL url)  {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
            conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private void insertIntoTable(List<News>newsList) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        try {
            for (News news : newsList) {
                Log.i(TAG, "Scheduling insert: entry_id=" + news.getNewsID());
                batch.add(ContentProviderOperation.newInsert(NewsContract.News.CONTENT_URI)
                        .withValue(NewsContract.News.COLUMN_NAME_ENTRY_ID, news.getNewsID())
                        .withValue(NewsContract.News.COLUMN_NAME_TITLE, news.getNewsTitle())
                        .withValue(NewsContract.News.COLUMN_NAME_DESCRIPTION, news.getNewsDescription())
                        .withValue(NewsContract.News.COLUMN_NAME_IMAGE_LINK, news.getNewsImageURL())
                        .withValue(NewsContract.News.COLUMN_NAME_IS_BOOKMARKED, news.isBookmarked())
                        .withValue(NewsContract.News.COLUMN_NAME_NEWS_CATEGORY, news.getNewsCategory())
                        .withValue(NewsContract.News.COLUMN_NAME_NEWS_TYPE, news.getNewsType())
                        .withValue(NewsContract.News.COLUMN_NAME_LINK, news.getNewsURL())
                        .withValue(NewsContract.News.COLUMN_NAME_IS_DELETED, news.isDeleted())
                        .withValue(NewsContract.News.COLUMN_NAME_DATE_PUBLISHED, news.getDatePublished())
                        .build());

            }
            Log.i(TAG, "Merge solution ready. Applying batch update");
            mContentResolver.applyBatch(NewsContract.CONTENT_AUTHORITY, batch);
            mContentResolver.notifyChange(
                    NewsContract.News.CONTENT_URI, // URI where data was modified
                    null,                           // No local observer
                    false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
