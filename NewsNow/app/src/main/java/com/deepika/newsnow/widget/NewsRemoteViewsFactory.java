package com.deepika.newsnow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deepika.newsnow.NewsDetail;
import com.deepika.newsnow.NewsNowWidget;
import com.deepika.newsnow.R;
import com.deepika.newsnow.pojo.News;
import com.deepika.newsnow.provider.NewsContract;
import com.deepika.newsnow.util.NewsNowConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/2/2017.
 */

public class NewsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int mCount = 10;
    private List<News> mWidgetItems = new ArrayList<News>();
    private Context mContext;
    private int mAppWidgetId;

    public NewsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.newslist);
        rv.setTextViewText(R.id.newsTitle, mWidgetItems.get(position).getNewsTitle());
        /*Intent intent = new Intent(mContext,NewsDetail.class);

        */Bundle extras = new Bundle();
        extras.putString(NewsNowConstants.NEWSIMAGEURL,mWidgetItems.get(position).getNewsImageURL());
        extras.putString(NewsNowConstants.TITLE,mWidgetItems.get(position).getNewsTitle());
        extras.putString(NewsNowConstants.DESCRIPTION,mWidgetItems.get(position).getNewsDescription());

        extras.putInt(NewsNowWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.setAction(NewsNowWidget.TOAST_ACTION);
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.newsTitle, fillInIntent);

        Log.v("newsremoteviewsfac","ondatasetchaged");

        return  rv;

    }

    @Override
    public int getViewTypeCount() {
        return mWidgetItems.size();
    }

    @Override
    public void onDataSetChanged() {

        String selection = NewsContract.News.COLUMN_NAME_IS_BOOKMARKED+ "=?";
        String [] selctionArgs= {"1"};
        String [] projection = {NewsContract.News.COLUMN_NAME_TITLE,NewsContract.News.COLUMN_NAME_DESCRIPTION,NewsContract.News.COLUMN_NAME_IMAGE_LINK,NewsContract.News.COLUMN_NAME_ENTRY_ID};
        Cursor cursor = mContext.getContentResolver().query(NewsContract.News.CONTENT_URI,projection,selection,selctionArgs,null);
        mWidgetItems.clear();
        while (cursor.moveToNext()){
            News n = new News();
            n.setNewsTitle(cursor.getString(cursor.getColumnIndex(NewsContract.News.COLUMN_NAME_TITLE)));
            n.setNewsID(cursor.getString(cursor.getColumnIndex(NewsContract.News.COLUMN_NAME_ENTRY_ID)));
            mWidgetItems.add(n);
        }

        Log.v("newsremoteviewsfac","ondatasetchaged");
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

}
