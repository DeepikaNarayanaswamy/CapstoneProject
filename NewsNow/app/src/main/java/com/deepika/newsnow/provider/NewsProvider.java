package com.deepika.newsnow.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deepika.newsnow.util.SelectionBuilder;

/**
 * Created by user on 3/26/2017.
 */

public class NewsProvider extends ContentProvider {
    NewsDatabase mDatabaseHelper;

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = NewsContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /entries
     */
    public static final int ROUTE_NEWS_ENTRIES = 1;

    /**
     * URI ID for route: /entries/{ID}
     */
    public static final int ROUTE_NEWS_ENTRIES_ID = 2;

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "newsList", ROUTE_NEWS_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "newsList/*", ROUTE_NEWS_ENTRIES_ID);
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_NEWS_ENTRIES_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(NewsContract.News._ID + "=?", id);
            case ROUTE_NEWS_ENTRIES:
                // Return all known entries.
                builder.table(NewsContract.News.TABLE_NAME)
                        .where(selection, selectionArgs);
                Log.v("PROVIER",builder.toString());
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new NewsDatabase(getContext());
        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_NEWS_ENTRIES:
                long id = db.insertOrThrow(NewsContract.News.TABLE_NAME, null, contentValues);
                result = Uri.parse(NewsContract.News.CONTENT_URI + "/" + id);
                break;
            case ROUTE_NEWS_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        int update = db.update(NewsContract.News.TABLE_NAME,contentValues,s,strings);
        return update;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_NEWS_ENTRIES:
                return NewsContract.News.CONTENT_TYPE;
            case ROUTE_NEWS_ENTRIES_ID:
                return NewsContract.News.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    // Here we are defining the NEWS database

    static class NewsDatabase extends SQLiteOpenHelper {
        /** Schema version. */
        public static final int DATABASE_VERSION = 3;
        /** Filename for SQLite file. */
        public static final String DATABASE_NAME = "news.db";
        private static final String TYPE_DATE = " DATE";
        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String TYPE_BOOLEAN = " BOOLEAN";
        private static final String COMMA_SEP = ",";
        /** SQL statement to create "entry" table. */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + NewsContract.News.TABLE_NAME + " (" +
                        NewsContract.News._ID + " INTEGER PRIMARY KEY," +
                        NewsContract.News.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_TITLE    + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_LINK + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_DESCRIPTION + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_IMAGE_LINK + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_NEWS_CATEGORY + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_NEWS_TYPE + TYPE_TEXT + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_DATE_PUBLISHED + TYPE_DATE + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_IS_BOOKMARKED + TYPE_BOOLEAN + COMMA_SEP +
                        NewsContract.News.COLUMN_NAME_IS_DELETED + TYPE_BOOLEAN +
                        ");";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + NewsContract.News.TABLE_NAME;

        public NewsDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }





}
