package com.example.android.newsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Haiyan on 7/28/17.
 * Database Helper method to create database and for update
 */

public class NewsAppDBHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "newsapp.db";
    static final int DATABASE_VERSION = 5;

    static final String TAG = "NewsAppDBHelper";

    public NewsAppDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                NewsAppContract.NewsAppEntry.TABLE_NAME + " (" +
                NewsAppContract.NewsAppEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsAppContract.NewsAppEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsAppContract.NewsAppEntry.COLUMN_URL + " TEXT NOT NULL, " +
                NewsAppContract.NewsAppEntry.COLUMN_URL_IMAGE + " TEXT, " +
                NewsAppContract.NewsAppEntry.COLUMN_AUTHOR + " TEXT, " +
                NewsAppContract.NewsAppEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NewsAppContract.NewsAppEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        Log.d(TAG, "SQL " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsAppContract.NewsAppEntry.TABLE_NAME);
        onCreate(db);
    }
}