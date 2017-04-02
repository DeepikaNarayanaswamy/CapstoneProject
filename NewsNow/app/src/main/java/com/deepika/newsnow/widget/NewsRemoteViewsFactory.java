package com.deepika.newsnow.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deepika.newsnow.R;
import com.deepika.newsnow.pojo.News;

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
        News n = new News();
        n.setNewsTitle("abcd");
        News n1 = new News();
        n1.setNewsTitle("213255");
        mWidgetItems.add(n);
        mWidgetItems.add(n1);
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
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.newslist);
        rv.setTextViewText(R.id.newsTitle, mWidgetItems.get(position).getNewsTitle());
        return  rv;

    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public int getCount() {
        return mCount;
    }
}
