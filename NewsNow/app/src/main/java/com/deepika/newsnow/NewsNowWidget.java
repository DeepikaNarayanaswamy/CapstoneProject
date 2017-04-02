package com.deepika.newsnow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.deepika.newsnow.widget.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class NewsNowWidget extends AppWidgetProvider {

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
        // Construct the RemoteViews object

        //views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);

        // Instruct the widget manager to update the widget
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


}

