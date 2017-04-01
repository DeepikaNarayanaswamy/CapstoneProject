package com.deepika.newsnow;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepika.newsnow.provider.NewsContract;
import com.deepika.newsnow.util.NewsNowConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

public class NewsDetail extends AppCompatActivity {
    public static final String TAG = "NewsDetail";
    String newsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_bookmark_white_18dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBookmark(newsId);

            }
        });
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            ImageView imageView  = (ImageView)findViewById(R.id.newsImage);
            Picasso.with(this)
                    .load(b.getString(NewsNowConstants.NEWSIMAGEURL))
                    .into(imageView);
            TextView titleTV = (TextView)findViewById(R.id.newsTITLE);
            titleTV.setText(b.getString(NewsNowConstants.TITLE));
            TextView descTV = (TextView)findViewById(R.id.newsDescription);
            descTV.setText(b.getString(NewsNowConstants.DESCRIPTION));
            newsId = b.getString(NewsNowConstants.NEWS_ID);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newsdetail_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                TextView newsTitle = (TextView)findViewById(R.id.newsTITLE);
                String shareBodyText = newsTitle.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharing_option)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveBookmark(String newsId){
        ContentValues mUpdateValues = new ContentValues();
        Log.v(TAG,"To be bookmarked="+newsId);
        // Defines selection criteria for the rows you want to update
        String mSelectionClause = NewsContract.News.COLUMN_NAME_ENTRY_ID +  " = ?";        String[] mSelectionArgs = {newsId};

        // Defines a variable to contain the number of updated rows
        int mRowsUpdated = 0;

        /*
         * Sets the updated value and updates the selected words.
         */
        mUpdateValues.put(NewsContract.News.COLUMN_NAME_IS_BOOKMARKED,true);
        String [] projection = {NewsContract.News.COLUMN_NAME_IS_BOOKMARKED};

        Cursor cursor = getContentResolver().query(NewsContract.News.CONTENT_URI,projection,mSelectionClause,mSelectionArgs,null);
        Log.v(TAG,"Number of records with newsid="+cursor.getCount());

        mRowsUpdated = getContentResolver().update(
                NewsContract.News.CONTENT_URI,   // the user dictionary content URI
                mUpdateValues,                       // the columns to update
                mSelectionClause,                    // the column to select on
                mSelectionArgs                      // the value to compare to
        );
        Log.v(TAG,"Number of rows updated ="+mRowsUpdated);
    }

}
