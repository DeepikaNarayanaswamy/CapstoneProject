package com.deepika.newsnow.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 3/26/2017.
 */

public class NewsContract {

    private NewsContract(){}
    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.deepika.newsnow";

    /**
     * Base URI. (content://com.example.android.basicsyncadapter)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "newsList";

    /**
     * Columns supported by "entries" records.
     */
    public static class News implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.newsnow.newsList";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.newsnow.news";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "news" resources.
         */
        public static final String TABLE_NAME = "news";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_ENTRY_ID = "news_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_TITLE = "title";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_LINK = "news_link";

        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE_LINK = "news_image_link";
        public static final String COLUMN_NAME_IS_DELETED = "is_deleted";
        public static final String COLUMN_NAME_DATE_PUBLISHED = "date_published";
        public static final String COLUMN_NAME_NEWS_CATEGORY = "news_category";
        public static final String COLUMN_NAME_NEWS_TYPE = "news_type";
        public static final String COLUMN_NAME_IS_BOOKMARKED = "is_bookmarked";

    }
}

