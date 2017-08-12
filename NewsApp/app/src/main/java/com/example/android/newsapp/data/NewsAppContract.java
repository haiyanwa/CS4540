package com.example.android.newsapp.data;

import android.provider.BaseColumns;

/**
 * Created by Haiyan on 7/28/17.
 * Database contract
 */

public class NewsAppContract {

    public static final class NewsAppEntry implements BaseColumns {
        public static final String TABLE_NAME = "newsapp";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URL_IMAGE = "image_url";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
