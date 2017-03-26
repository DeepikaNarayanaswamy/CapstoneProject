package com.deepika.newsnow.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.deepika.newsnow.util.NewsNowConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by user on 3/26/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v("sync adapter","sync adapter called");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String category = preferences.getString("news_category_list",null);
        StringBuilder stringBuilder = new StringBuilder(NewsNowConstants.NEWSORGURL);
        stringBuilder.append(NewsNowConstants.QUESTION_MARK);
        switch (category){
            case NewsNowConstants.NEWS_CATEGORY_GENERAL : stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + " = " + NewsNowConstants.THE_HINDU);
                break;
            case NewsNowConstants.NEWS_CATEGORY_SPORTS : stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + " = " + NewsNowConstants.ESPN);
                break;

            case NewsNowConstants.NEWS_CATEGORY_WILDLIFE : stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + " = " + NewsNowConstants.NATIONAL_GEOGRAPHIC);
                break;
            case NewsNowConstants.NEWS_CATEGORY_TECHNOLOGY : stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + " = " + NewsNowConstants.TECH_CRUNCH);
                break;
            default:stringBuilder.append(NewsNowConstants.NEWS_SOURCE_PARAMETER + " = " + NewsNowConstants.THE_HINDU);
                break;
        }
        stringBuilder.append(NewsNowConstants.AND+NewsNowConstants.NEWS_SORTBY_PARAMETER+"="+NewsNowConstants.NEWS_SORTBY_TOP);
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


           Log.v(TAG,buffer.toString());


       }catch (Exception ex){
           ex.printStackTrace();
       }
    }


    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
