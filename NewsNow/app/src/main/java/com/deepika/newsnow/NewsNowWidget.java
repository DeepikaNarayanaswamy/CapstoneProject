package com.deepika.newsnow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.deepika.newsnow.widget.NewsRemoteViewsFactory;
import com.deepika.newsnow.widget.WidgetService;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Implementation of App Widget functionality.
 */
public class NewsNowWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String TAG = "NewsNowWidget";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

         // Set up the intent that starts the StackViewService, which will
         // provide the views for this collection.
         Intent intent = new Intent(context, WidgetService.class);
         // Add the app widget ID to the intent extras.
         intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
         intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
         // Instantiate the RemoteViews object for the app widget layout.
         RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.news_now_widget);
         // Set up the RemoteViews object to use a RemoteViews adapter.
         // This adapter connects
         // to a RemoteViewsService  through the specified intent.
         // This is how you populate the data.
         rv.setRemoteAdapter(appWidgetId, R.id.widgetList, intent);


         CharSequence widgetText = context.getString(R.string.appwidget_text);
        final Intent onItemClick = new Intent(context, NewsNowWidget.class);
        onItemClick.setAction(TOAST_ACTION);
        onItemClick.setData(Uri.parse(onItemClick
                .toUri(Intent.URI_INTENT_SCHEME)));
        final PendingIntent onClickPendingIntent = PendingIntent
                .getBroadcast(context, 0, onItemClick,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widgetList,
                onClickPendingIntent);

         appWidgetManager.updateAppWidget(appWidgetId, rv);
         appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widgetList);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG,"onReceive");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if(NewsNowWidget.TOAST_ACTION.equals(intent.getAction())) {
            Log.v(TAG, "toast");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Bundle extras = intent.getExtras();
            Intent i = new Intent(context,NewsDetail.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtras(intent.getExtras());
            context.startActivity(i);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);

        }
        super.onReceive(context, intent);

    }


}

